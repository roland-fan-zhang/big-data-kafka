package fr.uge.kafka;

import org.apache.kafka.common.serialization.Serializer;
import tools.jackson.databind.json.JsonMapper;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class StudentJsonSerializer implements Serializer<Student> {
    @Override
    public byte[] serialize(String s, Student student) {
        Objects.requireNonNull(s);
        Objects.requireNonNull(student);
        return new JsonMapper().writeValueAsBytes(student);
    }
}
