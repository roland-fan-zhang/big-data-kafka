## Rapport TP1 Kafka - Big data

### 1.3 Manipulation des Topics

Les commandes que j'ai trouvé se trouve dans cette page de la documentation : 

https://docs.confluent.io/kafka/operations-tools/kafka-tools.html

#### 1.3.1 Créer un topic

```shell
roland@roland kafka_2.13-4.1.1 % bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --topic test-topic --partitions 3
```

#### 1.3.2 Lister les topics

```shell
roland@roland kafka_2.13-4.1.1 % bin/kafka-topics.sh --list --bootstrap-server localhost:9092                              
test-topic
```

#### 1.3.3 Décrire le topic

```shell
roland@roland kafka_2.13-4.1.1 % bin/kafka-topics.sh --describe --bootstrap-server localhost:9092
Topic: test-topic       TopicId: sE05QnUPQQ-FLs5ekB8NLQ PartitionCount: 3       ReplicationFactor: 1    Configs: min.insync.replicas=1,segment.bytes=1073741824
        Topic: test-topic       Partition: 0    Leader: 1       Replicas: 1     Isr: 1  Elr:    LastKnownElr: 
        Topic: test-topic       Partition: 1    Leader: 1       Replicas: 1     Isr: 1  Elr:    LastKnownElr: 
        Topic: test-topic       Partition: 2    Leader: 1       Replicas: 1     Isr: 1  Elr:    LastKnownElr: 
```

### 1.4 Producteur et Consommateur en ligne de commande

#### 1.4.1 Démarrer un producteur

```shell
bin/kafka-console-producer.sh --topic test-topic --bootstrap-server localhost:9092
```

#### 1.4.2 Démarrer un consommateur

Pourquoi les messages envoyez précédemment ne sont-ils pas reçu ?

On n'a pas indiqué à partir de quand le consumer lit les données, il manque un argument.

#### 1.4.3 Expérimentez

```shell
roland@roland kafka_2.13-4.1.1 % bin/kafka-console-consumer.sh --topic test-topic --from-beginning --bootstrap-server localhost:9092
```

Que se passe-t-il ?

Le paramètre affiche l'historique des messages depuis la création du topic et affiche les futurs messages.

#### 1.4.4 consumer group

```shell
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic --group mon-groupe
```

#### 1.4.5 Questions à répondre

Comment sont-ils distribués ?

Les consommateurs sont distribués grâce à un mécanisme de load balancing.

## Partie 2

```shell
sh-5.1$ kafka-console-producer --topic etudiants --bootstrap-server localhost:9092
```




