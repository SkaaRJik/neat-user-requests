package ru.filippov.neat.serializator;

import java.io.IOException;
import java.io.Serializable;

public interface Serializator {
    byte[] serialize(Object object) throws IOException;
    Object deserialize(byte[] bytes);
}
