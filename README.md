# Kafka Streams System Integration Demo
A demonstration on how to implement [enterprise integration patterns (EIP)](https://camel.apache.org/components/latest/eips/enterprise-integration-patterns.html) with Kafka Streams

## How to Use

How to test this?

1. [Install Docker Compose](https://docs.docker.com/compose/install/)
2. Clone this repository
3. Open a command prompt or Terminal window   
4. Start all containers (Kafka, PostgreSQL, and Zookeeper) with the docker compose script in the project root directory:
   `docker-compose up -d`
5. Create the Kafka topic transformations-input-topic 
  ```
  docker-compose exec kafka1 kafka-topics \
  --create \
  --bootstrap-server localhost:9092 \
  --replication-factor 1 \
  --partitions 1 \
  --topic transformations-input-topic
  ```
6. Create a Kafka console consumer to monitor the output
  ```
  docker-compose exec kafka1 kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic transformations-output-topic \
  --from-beginning
  ``` 
7. Run ./gradlew bootRun in the root project directory
8. Insert data into the database (your sequence name may be different)

   ```insert into phone (id, country_code, phone_number, phone_type) VALUES (nextval('hibernate_sequence'), 'US', '917-555-8888', 'Home');
    insert into address (id, address_type, city, postal_code, state, street) VALUES (nextval('hibernate_sequence'), 'Home', 'Brooklyn', '11225', 'NY', '555 Main Street');
    insert into address_phones (address_id, phones_id) values (currval('hibernate_sequence'), lastval()-1);

    insert into phone (id, country_code, phone_number, phone_type) VALUES (nextval('hibernate_sequence'), 'US', '203-555-8888', 'Business');
    insert into address (id, address_type, city, postal_code, state, street) VALUES (nextval('hibernate_sequence'), 'Business', 'New York', '10001', 'NY', '55 Hudson Yards');
    insert into address_phones (address_id, phones_id) values (currval('hibernate_sequence'), lastval()-1);

    insert into employee (id, department_id, first_name, last_name, manager_id, years_of_service) VALUES (nextval('hibernate_sequence'), 'HR', 'Frodo', 'Baggins', null, 23);
    insert into employee_addresses (employee_id, addresses_id) values (currval('hibernate_sequence'), lastval()-1);

    insert into employee (id, department_id, first_name, last_name, manager_id, years_of_service) VALUES (nextval('hibernate_sequence'), 'IT', 'Darth', 'Vadar', null, 41);
    insert into employee_addresses (employee_id, addresses_id) values (currval('hibernate_sequence'), lastval()-4);```

9. Drop a file in the src/main/data directory (See sample)

## Troubleshoot

1. The file used to kick off the process has to have at least 3 comma separated values in each row (Employee Id, first name, last name)
2. The application will filter employee last names beginning with a 'B' or 'D'.  If no data is published to the topic 'transformations-output-topic', check the file used to start the process.

## Technology Stack
1. Java 11 (Java 8 should work fine)
2. Spring Boot 
3. Spring Data JPA   
4. Apache Camel
5. Apache Kafka
6. PostgreSQL database
