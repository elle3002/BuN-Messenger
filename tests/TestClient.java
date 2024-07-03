import yourApp.PDUMessage;
import yourApp.ProtocolEngine;

import java.io.IOException;
import java.net.Socket;

public class TestClient {
    public static void main(String[] args) throws IOException, IOException {
        String serverIP = "127.0.0.1"; // IP-Adresse des Testservers
        int port = 123; // Gleicher Port wie in deinem CommunicationManager
        Socket socket = new Socket(serverIP, port);

        // Nachricht senden
        PDUMessage message = new PDUMessage("127.0.0.1", "Hallo Welt!");
        ProtocolEngine.serialisiereMessage(socket.getOutputStream(), message);

        System.out.println("Nachricht gesendet an " + serverIP);

        socket.close();
    }
}