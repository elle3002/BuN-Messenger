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
            //open ServerSocket
            System.out.println("Horche...Warte auf Client");
            ServerSocket serverSocket = new ServerSocket(portnumber);
            System.out.println("Server auf Port:" + portnumber);
            Socket s;

            s = serverSocket.accept();

            System.out.println("Verbindung aufgebaut");

            //Datenlesen und deserialisieren
            InputStream is = s.getInputStream();
            PDUInterface received = ProtocolEngine.deserialisiere(is);

            //Ausgeben auf der Konsole
            ConsoleOutputManager.printReceivedData(received);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static OutputStream connectToServer(String sendeAnIP, int portNumber) throws IOException {

        //Socket eröffnen und Outputstream zurückgeben
        Socket s = new Socket(sendeAnIP, portNumber);
        OutputStream os = s.getOutputStream();
        return os;
    }
}
