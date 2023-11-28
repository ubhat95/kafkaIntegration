# Kafka SpringBoot

This spring boot app allows you to rake in recent changes from Wikimedia, produces it onto Kafka, and has an OpenSearch consumer indexing it.
Has sample producer and consumer using both Kafka API and spring-boot annotations <br><br>
![image](https://github.com/ubhat95/kafkaSpringBoot/assets/53697553/46cefa40-3599-4980-8a2b-032a8c6c7460)

Steps:  
1.   Install[ Docker Desktop](https://www.docker.com/products/docker-desktop/) <br>
2.   Run  `docker compose -f  opensearch-docker.yml up`  to start openSearch on port 9200, dev_tools  available on `http://localhost:5601/app/dev_tools#/console` <br>
3.   Run  `docker compose -f  zk-single-kafka-single.yml up` to start kafka on 9092, create `wikimedia_topic` you can find the commands at the end. <br>
4.   Run project as springboot application and `http://localhost:8080/wmos/run` on [postman](https://www.postman.com/downloads/) <br>




# Kafka Integration

more kafka docker at  : https://www.conduktor.io/kafka/starting-kafka/

![image](https://github.com/ubhat95/kafkaIntegration/assets/53697553/fd95895c-a60d-42d0-9157-b5899d1cf4d1)

Notes :
`Producers` - > Kafka - > `Consumers`;
`Topics` -> `partition 0` ..... partition n; 
(topic analogous to a table)

`Producers` produce` kafkaMessage` and load them onto a `partition`. kM has {key, value||compression alg||headers|partition, offset}.
If kM has a key then hashed onto the same partition; If no key -> round-robin
kM is a byte sequence -> producer messages need serialization. 

`Consumer` - pull model - they know which broker to read from and are smart enough to recover from failures
kMs are read in order by consumers. kM needs deserialization to be read, consumers need to know the structure of messages they want to read
c->p 1:many; p->c 1:1 within a consumer group. usually have distinct consumer groups for different services


Kafka has an internal topic `__consumer_offsets` -> keeps tabs on offsets read in the partition by different consumers; fault tolerance. 

`cluster` has `brokers` - `bootstrap server` (each one them with an id and they have topic partitions); if you connect to any broker, the client will have access to the entire cluster
Topics have a `replication factor` and are replicated on brokers for fault tolerance; there are leaders and ISR(in sync) OSRs; only write data on to leader then let Kafka do its job
`Zookeepers` manages brokers until 2.x


docker for kafka
	`docker-compose -f zk-single-kafka-single.yml up -d`<br>
	`docker compose -f zk-single-kafka-multiple.yml up`<br>
	`docker compose -f zk-single-kafka-multiple.yml down`<br>


<br>run commands<br>
	`docker exec -it kafka1 /bin/bash` <br>
  
<br>create topic<br>
	`kafka-topics --bootstrap-server localhost:9092 --topic first_topic --create --partitions 3 --replication-factor 1`<br>

<br>describe<br>
	`kafka-topics --bootstrap-server localhost:9092 --describe --topic first_topic`<br>
	
<br>list topic<br>
	`kafka-topics --bootstrap-server localhost:9092 --list`<br>
 
<br>delete topic<br>
	`kafka-topics --bootstrap-server localhost:9092 --delete --topic first_topic`<br>
	
<br>alter topic<br>
	`kafka-topics.sh --bootstrap-server localhost:9092 --alter --topic first_topic --partitions 5`<br>

<br>producer <br>
	`kafka-console-producer --bootstrap-server localhost:9092 --topic first_topic`<br>
	`kafka-console-producer --bootstrap-server localhost:9092 --topic first_topic --property parse.key=true --property key.separator=:`<br>
	`kafka-console-producer --bootstrap-server localhost:9092 --topic your_topic --property "partition=0"`<br>

<br>consumer<br>
	`kafka-console-consumer --bootstrap-server localhost:9092 --topic first_topic --from-beginning`<br>
	`kafka-console-consumer --bootstrap-server localhost:9092 --topic first_topic --from-beginning --partition 1`<br>
	`kafka-console-consumer --bootstrap-server localhost:9092 --topic first_topic --formatter kafka.tools.DefaultMessageFormatter --property print.timestamp=true --property print.key=true --property print.value=true --from-beginning --property print.offset=true --property print.partition=true --property print.headers=true	`<br>		
	
<br>consumer within consumer group<br>
	`kafka-console-consumer --bootstrap-server localhost:9092 --topic first_topic --group my-first-application`<br> 
	`kafka-console-consumer --bootstrap-server localhost:9092 --topic first_topic --group my-first-application`<br>
	
<br>consumer group management<br>
	`kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group my-first-application`<br>
	`kafka-consumer-groups --bootstrap-server localhost:9092 --group my-first-application --reset-offsets --to-earliest --execute --topic first_topic`<br>
	`kafka-consumer-groups --bootstrap-server localhost:9092 --group my-first-application --reset-offsets --shift-by -2 --execute --topic first_topic`<br>

<br>list all groups<br>
	`kafka-consumer-groups --bootstrap-server localhost:9092 --list --state`<br>
	
<br>describe<br>
	`kafka-consumer-groups --bootstrap-server localhost:9092 --describe --all-groups  --state`<br>
	
<br>delete<br>
	`kafka-consumer-groups --bootstrap-server localhost:9092 --delete --group my-first-application`<br>
