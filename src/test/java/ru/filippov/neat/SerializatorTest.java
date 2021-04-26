package ru.filippov.neat;

import jcifs.smb.SmbException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.filippov.neat.samba.SambaWorker;
import ru.filippov.neat.serializator.Serializator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class SerializatorTest {

    @Autowired
    Serializator serializator;

    @Test
    public void serializeMap() throws IOException {
        Map<String, Object> map = new HashMap<>(5);
        map.put("Hello", "Hi!");

        byte[] serialize = serializator.serialize(map);

        Assertions.assertNotEquals(0,serialize.length);
    }

    @Test
    public void deserializeMap() throws IOException {
        byte[] serializedMap = { -84, -19, 0, 5, 115, 114, 0, 17, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46, 72, 97, 115, 104, 77, 97, 112, 5, 7, -38, -63, -61, 22, 96, -47, 3, 0, 2, 70, 0, 10, 108, 111, 97, 100, 70, 97, 99, 116, 111, 114, 73, 0, 9, 116, 104, 114, 101, 115, 104, 111, 108, 100, 120, 112, 63, 64, 0, 0, 0, 0, 0, 6, 119, 8, 0, 0, 0, 8, 0, 0, 0, 1, 116, 0, 5, 72, 101, 108, 108, 111, 116, 0, 3, 72, 105, 33, 120};

        Map<String, Object> deserialize = (Map<String, Object>) serializator.deserialize(serializedMap);

        Assertions.assertNotNull(deserialize);
    }





}
