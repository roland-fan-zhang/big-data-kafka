package fr.uge.kafka;

import org.apache.kafka.common.serialization.Deserializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

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
