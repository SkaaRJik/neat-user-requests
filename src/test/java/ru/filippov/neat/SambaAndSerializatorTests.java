package ru.filippov.neat;

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
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class SambaAndSerializatorTests {

    @Autowired
    SambaWorker sambaWorker;

    @Autowired
    Serializator serializator;

    @Test
    public void readAndDeserializeFile() throws IOException {

        /*byte[] bytes = sambaWorker.readFile("test.ser");

        Map<String, Object> res = (Map<String, Object>) serializator.deserialize(bytes);

        Assertions.assertEquals("Hi!", res.get("Hello"));*/
    }

    @Test
    public void serializeAndWriteFile() throws IOException {
        /*Map<String, Object> map = new HashMap<>(5);
        map.put("Hello", "Hi!");

        byte[] serialize = serializator.serialize(map);

        Assertions.assertDoesNotThrow(() -> {
            sambaWorker.writeBytesArray("test.ser", serialize);
        });*/
    }


}
