package fr.uge.kafka;

import org.apache.kafka.common.serialization.Deserializer;
import tools.jackson.databind.json.JsonMapper;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class StudentJsonDeserializer implements Deserializer<Student> {
    @Override
    public Student deserialize(String s, byte[] bytes) {
        Objects.requireNonNull(s);
        Objects.requireNonNull(bytes);
        return new JsonMapper().readValue(bytes, Student.class);
    }
}
