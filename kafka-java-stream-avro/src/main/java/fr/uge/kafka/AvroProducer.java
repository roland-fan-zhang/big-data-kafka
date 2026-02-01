package fr.uge.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

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