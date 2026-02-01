package fr.uge.kafka;

import org.apache.kafka.common.serialization.Serializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

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
