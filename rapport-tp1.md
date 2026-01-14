## Rapport TP1 Kafka - Big data

### 1.3 

https://docs.confluent.io/kafka/operations-tools/kafka-tools.html

```shell
roland@roland kafka_2.13-4.1.1 % bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --topic test-topic --partitions 3
```

```shell
roland@roland kafka_2.13-4.1.1 % bin/kafka-topics.sh --list --bootstrap-server localhost:9092                              
test-topic
```

```shell
roland@roland kafka_2.13-4.1.1 % bin/kafka-topics.sh --describe --bootstrap-server localhost:9092
Topic: test-topic       TopicId: sE05QnUPQQ-FLs5ekB8NLQ PartitionCount: 3       ReplicationFactor: 1    Configs: min.insync.replicas=1,segment.bytes=1073741824
        Topic: test-topic       Partition: 0    Leader: 1       Replicas: 1     Isr: 1  Elr:    LastKnownElr: 
        Topic: test-topic       Partition: 1    Leader: 1       Replicas: 1     Isr: 1  Elr:    LastKnownElr: 
        Topic: test-topic       Partition: 2    Leader: 1       Replicas: 1     Isr: 1  Elr:    LastKnownElr: 
```

### 1.4

```shell
bin/kafka-console-producer.sh --topic test-topic --bootstrap-server localhost:9092
```

Pourquoi les messages envoyez précédemment ne sont-ils pas reçu ?

On a pas indiqué comment le consumer traite les données, il manque un paramètre

```shell
roland@roland kafka_2.13-4.1.1 % bin/kafka-console-consumer.sh --topic test-topic --from-beginning --bootstrap-server localhost:9092
```

## Partie 2

```shell
sh-5.1$ kafka-console-producer --topic etudiants --bootstrap-server localhost:9092
```




