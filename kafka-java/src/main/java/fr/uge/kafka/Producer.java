package fr.uge.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

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
            System.out.println("partition : " + metadata.partition() + " offset : " + metadata.offset());
        } catch (ExecutionException | InterruptedException e) {
            throw new AssertionError(e);
        }
    }
}
