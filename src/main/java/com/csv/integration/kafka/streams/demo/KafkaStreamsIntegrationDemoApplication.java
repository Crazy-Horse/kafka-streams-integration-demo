package com.csv.integration.kafka.streams.demo;

import com.csv.integration.kafka.streams.demo.data.EmployeeDTO;
import com.csv.integration.kafka.streams.demo.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class KafkaStreamsIntegrationDemoApplication {

    private static final Logger log = LoggerFactory.getLogger(KafkaStreamsIntegrationDemoApplication.class);
    private final EmployeeService employeeService;
    //private final FileWatcherService fileWatcherService;

    public KafkaStreamsIntegrationDemoApplication(EmployeeService service) {
        this.employeeService = service;
    }

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(KafkaStreamsIntegrationDemoApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
        context.getBean(KafkaStreamsIntegrationDemoApplication.class).runDemo(context);

        context.close();
    }

    private void runDemo(ConfigurableApplicationContext applicationContext) {

        final Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "integration-example");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        // Since the input topic uses Strings for both key and value, set the default Serdes to String.
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        // Get the source stream.
        final StreamsBuilder builder = new StreamsBuilder();
        final KStream<String, String> source = builder.stream("transformations-input-topic");

        // Split the stream into two streams, one containing all valid records, and the other containing all other records.
        KStream<String, String>[] branches = source
                .branch((key, value) -> EmployeeDTO.validate(value), (key, value) -> true);
        KStream<String, String> validStream = branches[0];
        KStream<String, String> othersStream = branches[1];

        //Print each record to the console.
        //validStream.peek((key, value) -> log.info("key=" + key + ", value=" + value));

        // Map value into an Employee DTO
        KStream<String, EmployeeDTO> validEmpStream = validStream.map((key, value) -> KeyValue.pair(key, EmployeeDTO.newInstance(value)))

                // Remove any records from the valid stream where the employee's last name does not also start with "b" or "d".
                .filter((key, value) -> value.getLastName().toLowerCase().startsWith("d") ||
                        value.getLastName().toLowerCase().startsWith("b"))
                // Enrich Employee DTO with employee data from database
                .map((key, value) -> KeyValue.pair(key, employeeService.enrich(value)));

        //Print each record to the console.
        validEmpStream.peek((key, value) -> log.info("key=" + key + ", value=" + value));

        // Map value back into a string
        KStream<String, String> validEmployeeStream = validEmpStream.map((key, value) -> KeyValue.pair(key, value.toString()));

        //Output the transformed data to a topic.
        validEmployeeStream.to("transformations-output-topic");
        othersStream.to("bad-data-topic");

        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, props);
        // Print the topology to the console.
        System.out.println(topology.describe());
        final CountDownLatch latch = new CountDownLatch(1);

        // Attach a shutdown handler to catch control-c and terminate the application gracefully.
        Runtime.getRuntime().addShutdownHook(new Thread("integration-streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (final Throwable e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        System.exit(0);
    }
}
