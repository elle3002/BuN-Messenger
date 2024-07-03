import yourApp.ConsoleManager;
import yourApp.PDUInterface;
import yourApp.ProtocolEngine;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {
    public static void main(String[] args) throws IOException, IOException {
        int port = 123; // Gleicher Port wie in deinem CommunicationManager
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("TestServer läuft auf Port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Verbindung von " + clientSocket.getInetAddress());

            // Liest die empfangenen Daten
            InputStream is = clientSocket.getInputStream();
            PDUInterface reveivedData = ProtocolEngine.deserialisiere(is);
            ConsoleManager.printReceivedData(reveivedData);

            clientSocket.close();
        }
    }
}