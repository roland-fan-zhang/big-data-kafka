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

### 5. Crée un nouveau producer et consommateur pour envoyer des objets java byte à byte représentant des étudiants
Nous allons utiliser l'API des bytebuffer.
```java
public final class StudentSerializer implements Serializer<Student> {
    @Override
    public byte[] serialize(String s, Student student) {
        Objects.requireNonNull(s);
        Objects.requireNonNull(student);
        var fName = student.firstname().getBytes(StandardCharsets.UTF_8);
        var lName = student.lastname().getBytes(StandardCharsets.UTF_8);
        var degree = student.engineeringDegree().getBytes(StandardCharsets.UTF_8);
        var size = 4 + fName.length + 4 + lName.length + 4 + 4 + degree.length;
        var buffer = ByteBuffer.allocate(size);
        buffer.putInt(fName.length);
        buffer.put(fName);
        buffer.putInt(lName.length);
        buffer.put(lName);
        buffer.putInt(student.age());
        buffer.putInt(degree.length);
        buffer.put(degree);
        return buffer.array();
    }
}
```

```java
public final class StudentDeserializer implements Deserializer<Student> {
    @Override
    public Student deserialize(String s, byte[] bytes) {
        Objects.requireNonNull(bytes);
        Objects.requireNonNull(s);
        var buffer = ByteBuffer.wrap(bytes);
        var fNameLen = buffer.getInt();
        var fNameBytes = new byte[fNameLen];
        buffer.get(fNameBytes);
        var fName = new String(fNameBytes, StandardCharsets.UTF_8);
        var lNameLen = buffer.getInt();
        var lNameBytes = new byte[lNameLen];
        buffer.get(lNameBytes);
        var lName = new String(lNameBytes, StandardCharsets.UTF_8);
        var age = buffer.getInt();
        var degreeLen = buffer.getInt();
        var degreeBytes = new byte[degreeLen];
        buffer.get(degreeBytes);
        var degree = new String(degreeBytes, StandardCharsets.UTF_8);
        return new Student(fName, lName, age, degree);
    }
}
```

### 6. Crée un nouveau producer et consommateur pour envoyer des objets JSON représentant des étudiants

```java
public final class StudentJsonSerializer implements Serializer<Student> {
    @Override
    public byte[] serialize(String s, Student student) {
        Objects.requireNonNull(s);
        Objects.requireNonNull(student);
        return new JsonMapper().writeValueAsBytes(student);
    }
}
```

```java
public final class StudentJsonDeserializer implements Deserializer<Student> {
    @Override
    public Student deserialize(String s, byte[] bytes) {
        Objects.requireNonNull(s);
        Objects.requireNonNull(bytes);
        return new JsonMapper().readValue(bytes, Student.class);
    }
}
```

### 7. Créer un second consommateur dans le même groupe. Observez comment les partitions sont réparties.

En lançant un second consommateur, Kafka va diviser les partitions du topic entre les deux consommateurs. Si je possède qu'une seul partition, l'un deviendra inactif.

### 8. Expérimentez avec les partitions

Le topic avec 5 partitions semble plus rapide car la charge de travail est reparti sur les 5 partitions entre les consommateurs.
