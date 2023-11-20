# Kafka Integration

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
