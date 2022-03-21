# spring-boot-webflux-sink-only

* Run kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test
* Run KafkaTopicPopulator which will populate this topic with 100 events
* when you start in Spring Cloud Stream 2021.0.1 the offset order is out of order.  Stream 2020 works fine.