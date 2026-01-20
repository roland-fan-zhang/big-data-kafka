# Rapport TP1 Big data - Kafka Installation et commande de base

## Partie 1

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

### 2.1 Configuration Docker Compose

#### 2.1.1 fichier docker-compose-zookeeper.yml

```yaml
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.4.4
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000

  kafka:
    image: confluentinc/cp-kafka:7.4.4
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```

#### 2.1.2 fichier docker-compose-kraft.yml

```yaml
services:
  broker:
    image: confluentinc/cp-kafka:8.0.3
    container_name: broker
    ports:
      - "9092:9092"
      - "9101:9101"
    environment:
      KAFKA_NODE_ID: 1
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: 'CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT'
      KAFKA_ADVERTISED_LISTENERS: 'PLAINTEXT://broker:29092,PLAINTEXT_HOST://localhost:9092'
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS: 0
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
      KAFKA_JMX_PORT: 9101
      KAFKA_JMX_HOSTNAME: localhost
      KAFKA_PROCESS_ROLES: 'broker,controller'
      KAFKA_CONTROLLER_QUORUM_VOTERS: '1@broker:29093'
      KAFKA_LISTENERS: 'PLAINTEXT://broker:29092,CONTROLLER://broker:29093,PLAINTEXT_HOST://0.0.0.0:9092'
      KAFKA_INTER_BROKER_LISTENER_NAME: 'PLAINTEXT'
      KAFKA_CONTROLLER_LISTENER_NAMES: 'CONTROLLER'
      KAFKA_LOG_DIRS: '/tmp/kraft-combined-logs'
      # Remplacer CLUSTER_ID par un UUID unique comme dans la partie 1 si besoin de le changer
      CLUSTER_ID: 'MkU3OEVBNTcwNTJENDM3Qk'
    networks:
      - network

  kafka-ui:
    image: kafbat/kafka-ui:latest
    container_name: kafka-ui
    depends_on:
      - broker
    ports:
      - "9091:8080"
    environment:
      DYNAMIC_CONFIG_ENABLED: true
      KAFKA_CLUSTERS_0_NAME: broker
      KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: broker:29092
    networks:
      - network
    restart: unless-stopped

networks:
  network:
    driver: bridge
```

#### 2.1.3

```shell
roland@roland big-data-kafka % docker compose -f docker-compose-kraft.yml up -d
[+] Running 2/2
 ✔ Container kafka-ui  Started                                                                                                                           0.2s 
 ✔ Container broker    Started
```

#### 2.1.4

```shell
docker compose -f docker-compose-kraft.yml logs -f broker
```

```shell
broker  | [2026-01-20 09:19:54,655] INFO [BrokerServer id=1] Transition from STARTING to STARTED (kafka.server.BrokerServer)
broker  | [2026-01-20 09:19:54,655] INFO [BrokerServer id=1] Transition from STARTING to STARTED (kafka.server.BrokerServer)
broker  | [2026-01-20 09:19:54,655] INFO Kafka version: 8.0.3-ccs (org.apache.kafka.common.utils.AppInfoParser)
broker  | [2026-01-20 09:19:54,655] INFO Kafka commitId: dd72624fd95e6cbae53718866e0f67a6f7a1eb9d (org.apache.kafka.common.utils.AppInfoParser)
broker  | [2026-01-20 09:19:54,655] INFO Kafka startTimeMs: 1768900794655 (org.apache.kafka.common.utils.AppInfoParser)
broker  | [2026-01-20 09:19:54,655] INFO [KafkaRaftServer nodeId=1] Kafka Server started (kafka.server.KafkaRaftServer)
broker  | [2026-01-20 09:19:54,655] INFO [KafkaRaftServer nodeId=1] Kafka Server started (kafka.server.KafkaRaftServer)
```

### 2.2 Manipulation des Topics avec Docker

#### 2.2.1 Connexion au docker

```shell
roland@roland big-data-kafka % docker exec -it broker sh
sh-5.1$ 
```

#### 2.2.2 Créer un topic etudiants

```shell
sh-5.1$ kafka-console-producer --topic etudiants --bootstrap-server localhost:9092
>
```

#### 2.2.3 Lister les topics et décrire le topic etudiants

```shell
sh-5.1$ kafka-topics --list --bootstrap-server localhost:9092
etudiants
```

```shell
sh-5.1$ kafka-topics --describe --topic etudiants --bootstrap-server localhost:9092
Topic: etudiants        TopicId: ltK4OAbfQt6p_pcDN1In1Q PartitionCount: 1       ReplicationFactor: 1    Configs: 
        Topic: etudiants        Partition: 0    Leader: 1       Replicas: 1     Isr: 1  Elr:    LastKnownElr: 
```

#### 2.2.4 Créer un producteur Docker sur le topic etudiants

```shell
sh-5.1$ kafka-console-producer --topic etudiants --bootstrap-server localhost:9092
>{
"firstName": "test",
"lastName": "test",
"age": 21,
"engineeringDegree": "IT"
}
```

#### 2.2.5 Créer un consumateur Docker sur le topic etudiants

```shell
sh-5.1$ kafka-console-consumer --topic etudiants --from-beginning --bootstrap-server localhost:9092
{
"firstName": "test",
"lastName": "test",
"age": 21,
"engineeringDegree": "IT"
}
```

### Questions de synthèse

1. Quelle est la différence entre une partition et un topic ?

    Une partition fait partie d'un topic et permet de subdiviser un topic.

2. À quoi sert un consumer group ?

    Elle permet d'avoir plusieurs consumer pour un meme topic.

3. Quels sont les avantages de Docker pour le développement avec Kafka ?

    Elle permet de simplifier le déploiement et l'installation de Kafka.

4. Que se passe-t-il si on a plus de consommateurs que de partitions dans un groupe ?

    Il n'est pas possible d'avoir plus de consommateurs que de partitions, le surplus sont inactifs.    

5. Comment Kafka garantit-il l’ordre des messages ?

    Kafka ganratit l'ordre des évènements au sein d'une partition.