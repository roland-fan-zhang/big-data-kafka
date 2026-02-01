## Rapport TP2 Big data - Kafka avec Java

### 3. Créer dans une classe un producer qui écrit dans le topic "etudiants" crée au TP précédent

Lancer la classe avec un fonction main et vérifier via sh ou via kafka-ui qu'un message existe dans le topic etudiants

```java
public final class Producer {
    private static final String TOPIC = "etudiants";
    private static final String MESSAGE = "students";
    private static final String SERVER = "localhost:9092";

    void main() {
        var properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        var record = new ProducerRecord<String, String>(TOPIC, MESSAGE);

        try(var producer = new KafkaProducer<String, String>(properties)){
            var metadata = producer.send(record).get();
            IO.println("partition : " + metadata.partition() + " offset : " + metadata.offset());
        } catch (ExecutionException | InterruptedException e) {
            throw new AssertionError(e);
        }
    }
}
```

### 4. Créer dans une classe un consumer qui lit dans le topic "etudiants" crée au TP précédent

Lancer la classe avec un fonction main et vérifier dans les logs que le message crée précédemment est bien consommé. 

```java
public final class Consumer {
    private static final String TOPIC = "etudiants";
    private static final String SERVER = "localhost:9092";
    private static final String EARLIEST = "earliest";

    void main(){
        var properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, EARLIEST);

        try(var consumer = new KafkaConsumer<String, String>(properties)){
            consumer.subscribe(List.of(TOPIC));
            var records = consumer.poll(Duration.ofSeconds(10));
            for(var record : records) {
                IO.println(record.value());
            }
        }
    }
}
```

### 5. 