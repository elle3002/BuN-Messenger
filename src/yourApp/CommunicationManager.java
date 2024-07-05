package yourApp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CommunicationManager implements Runnable {

        private int portnumber;

    public CommunicationManager(int portnumber) {
        this.portnumber = portnumber;
    }

    @Override
    public void run() {

        try {
            //öffnet ServerSocket auf der angegebenen Portnummer und wartet auf Verbindung
            System.out.println("Horche...Warte auf Client");
            ServerSocket serverSocket = new ServerSocket(portnumber);//Server-Seite
            System.out.println("Server auf Port:" + portnumber);

            while (!serverSocket.isClosed()) {
                Socket s;

                s = serverSocket.accept();//Verbindung wird akzeptiert
                System.out.println("Verbindung aufgebaut");

                //Daten lesen aus Bytes und Deserialisieren
                InputStream is = s.getInputStream();
                PDUInterface received = ProtocolEngine.deserialisiere(is);

                //Ausgeben auf der Konsole
                ConsoleOutputManager.printReceivedData(received);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static OutputStream connectToServer(String sendeAnIP, int portNumber) throws IOException {
        //Schreiben von Daten in Bytes

        //neuer Socket wird geöffnet und Outputstream zurückgegeben
        Socket s = new Socket(sendeAnIP, portNumber);//Client-Seite
        OutputStream os = s.getOutputStream();

        return os;
    }
}
