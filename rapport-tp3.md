## Rapport TP3 Big data - Kafka Stream avec Avro

### 3. Créer une classe Avro pour représenter un étudiant.

Nous allons créer un fichier .avsc qui représente notre étudiant et pour générer la classe Java

```yaml
{
  "namespace": "fr.uge.avro",
  "type": "record",
  "name": "Student",
  "fields": [
    {"name": "firstName", "type": "string"},
    {"name": "lastName", "type": "string"},
    {"name": "age", "type": "int"},
    {"name": "engineeringDegree", "type": "string"}
  ]
}
```

### 4. Créer un producer qui envoie des objets Avro représentant des étudiants dans un topic Kafka nommé students-avro.

```java
public final class AvroProducer {
    void main(){
        var properties = new Properties();
        properties.put("bootstraps.servers", "localhost:9092");
        properties.put("key.serialiser", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        try(var producer = new KafkaProducer<>(properties)){
            var record = new ProducerRecord<Object, Object>("students-avro", new Student("foo", "bar", 24, "CS"));
            producer.send(record, ((recordMetadata, e) -> {
                if(e == null){
                    e.printStackTrace();
                }else{
                    System.out.println(recordMetadata);
                }
            }));
        }
    }
}
```
### 5. Créer une application Kafka Streams qui lit les messages du topic students-avro et affiche les informations des étudiants dans la console.

```java
public class StudentAvroSerde implements Serde<Student> {
    @Override
    public Serializer<Student> serializer() {
        return (_, student) -> student.toByteBuffer();
    }

    @Override
    public Deserializer<Student> deserializer() {
        return (_, student) -> Student.fromByteBuffer(ByteBuffer.wrap(student));
    }
}
```

```java
public class AvroConsumer {
    void main(){
        var properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-students-avro");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        try(var consumer = new KafkaConsumer<String, String>(properties)){
            consumer.subscribe(List.of("students-avro"));
            var records = consumer.poll(Duration.ofSeconds(10));
            for(var record : records) {
                IO.println(record.value());
            }
        }
    }
}
```

### Questions de synthèse 

On utilise Avro à la place de JSON principalement parce que c'est bien plus léger. C'est un format binaire qui rend les échanges plus rapides.

Le Serde contient un serialiseur et un deserialiseur qui va transformer une classe Java en binaires pour Kafka Streams car il manipule uniquement des octets.

`foreach()` est une action terminale utilisée pour effectuer des opérations à effet de bord, comme afficher les informations des étudiants dans la console. `to()` est utilisée pour diriger un flux de données vers un nouveau topic Kafka

Kafka Streams gère la tolérance aux pannes en sauvegardant l'état du traitement et les positions de lecture sous forme de offsets directement dans les topics Kafka.

Si on redémarre l'application, les données ne sont pas traitées depuis le début. Kafka identifie le dernier message traité avec grâce à l'offsets et reprend là où il c'est arrêté.
