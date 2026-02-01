package fr.uge.kafka;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.ByteBuffer;

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
