# trades-assignment
This assignment consists of two Spring-Boot based microservices.
- trade-capture - monitors given location (using Java WatchService class) and acts as a Kafka producer
- feed-service - listens to given Kafka topic and reads from it periodically

### Trade-capture
Code enables configurability and expandability with using of properties and interfaces.
Configuration parameters are:
- monitored location
- Kafka admin properties
- Kafka producer properties
- topics names (currently one topic, but architecture allows adding more together with appropriate processing classes)

Data is being sent to Kafka topic in batches (using batch.size and linger.ms) which allows higher throughput under heavy load.

Location monitoring is ready for both creation and modification events with the latter being left for future extension.

### Feed-service
It also relies heavily on properties, which allows degree of configurability. It comes with two Spring profiles: one defined for 'Service A' and one for 'Service B' listening on given Kafka topic using batch listening (by setting idle-between-polls to 300000ms and 60000ms respectively).
Configuration parameters are:
- Kafka consumer properties
- target data location and file name prefix
- batch interval
Both can be run simulataneousely and will receive the same data as long as their group remains the same.

### Running
To run above microservices:
#### Trade-capture
$ mvnw clean package spring-boot:repackage
$ java -jar target/trade-capture.jar
#### Feed-service
$ mvnw clean package spring-boot:repackage
$ java -jar target/feed-service.jar
