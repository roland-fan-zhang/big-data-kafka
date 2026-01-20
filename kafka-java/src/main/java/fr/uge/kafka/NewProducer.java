package fr.uge.kafka;

import org.apache.kafka.common.serialization.StringSerializer;

public final class NewProducer {
    private static final String TOPIC = "etudiants";
    private static final String JSON = """
            {
            "firstName": "Jean",
            "lastName": "Dupont",
            "age": 21,
            "engineeringDegree": "IT"
            }
            """;

    void main(){
        try(var serializer = new StringSerializer()){
            var bytes = serializer.serialize(TOPIC, JSON);
        }
    }
}
