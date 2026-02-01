package fr.uge.kafka;

import java.nio.ByteBuffer;

public record Student(String firstname, String lastname, int age, String engineeringDegree) {
    public static Student fromByteBuffer(ByteBuffer wrap) {
        return null;
    }

    public byte[] toByteBuffer() {
        return null;
    }
}
