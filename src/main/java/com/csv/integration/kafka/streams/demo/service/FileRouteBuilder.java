package com.csv.integration.kafka.streams.demo.service;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.stereotype.Component;

@Component
public class FileRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        CamelContext context = new DefaultCamelContext();

        from("file:{{file.drop.location}}?preMove=inprogress&move=.done")
                .log(LoggingLevel.INFO, "Processing file ${file:name}")
                .split().tokenize("\n").streaming()
                .setHeader(KafkaConstants.KEY, constant("Camel"))
                .log(LoggingLevel.INFO, "Processing message ${body}")
                .to("kafka:{{producer.topic}}?brokers={{kafka.bootstrap.url}}&keySerializer=org.apache.kafka.common.serialization.StringSerializer&valueSerializer=org.apache.kafka.common.serialization.StringSerializer");

    }

}
