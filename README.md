# Kafka SpringBoot
TODO : integrate with OpenAI so the user can query opensearch index by letting gpt know what they want


This spring boot app allows you to rake in recent changes from Wikimedia, produces it onto Kafka, and has an OpenSearch consumer indexing it.
Has sample producer and consumer using both Kafka API and spring-boot annotations <br><br>
![image](https://github.com/ubhat95/kafkaSpringBoot/assets/53697553/46cefa40-3599-4980-8a2b-032a8c6c7460)

![image](https://github.com/ubhat95/kafkaIntegration/assets/53697553/fd95895c-a60d-42d0-9157-b5899d1cf4d1)


Steps:  
1.   Install[ Docker Desktop](https://www.docker.com/products/docker-desktop/) <br>
2.   Run  `docker compose -f  opensearch-docker.yml up`  to start openSearch on port 9200, dev_tools  available on `http://localhost:5601/app/dev_tools#/console` <br>
3.   Run  `docker compose -f  zk-single-kafka-single.yml up` to start kafka on 9092
4.   Create topic `kafka-topics --bootstrap-server localhost:9092 --topic wikimedia_topic --create --partitions 3 --replication-factor 1`<br>
5.   Run project as springboot application and `http://localhost:8080/wmos/run` on [postman](https://www.postman.com/downloads/) <br>
