import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import yourApp.FileManager;
import yourApp.PDUFile;
import yourApp.PDUMessage;
import yourApp.ProtocolEngine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ProtocolEngineTest {

    @Test
    public void testSerialisiereMessage() throws IOException {
        PDUMessage message = new PDUMessage("192.168.1.1", "Hello, World!");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ProtocolEngine.serialisiereMessage(baos, message);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        PDUMessage deserializedMessage = (PDUMessage) ProtocolEngine.deserialisiere(bais);

        assertEquals(message.getType(), deserializedMessage.getType());
        assertEquals(message.getSenderIP(), deserializedMessage.getSenderIP());
        assertEquals(message.getMessage(), deserializedMessage.getMessage());
    }

    @Test
    public void testSerialisiereFile() throws IOException {
        String path = ".\\ressources\\10.pdf";
        byte[] imageData = FileManager.readFile(path);
        PDUFile file = new PDUFile("192.168.1.2", path, imageData);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ProtocolEngine.serialisiereFile(baos, file);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        PDUFile deserializedFile = (PDUFile) ProtocolEngine.deserialisiere(bais);

        assertEquals(file.getType(), deserializedFile.getType());
        assertEquals(file.getSenderIP(), deserializedFile.getSenderIP());
        assertArrayEquals(file.getData(), deserializedFile.getData());
    }

    @Test
    public void testFileManager() {
        System.out.println(FileManager.extractFileEndung("C:\\Users\\elias\\Desktop\\gitRepos\\BuN-Messenger\\ressources\\2.png"));
    }

}