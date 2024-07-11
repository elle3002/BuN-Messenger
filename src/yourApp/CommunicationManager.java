package yourApp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Verwaltet den Gesamten Connection-Part des Programms
 *      - zum einen stellt es eine Verbindung zu einem anderen Gerät her (connectToServer)
 *      - zum anderen wird diese Klasse als Thread gestartet und eröffnet einen Server an dem angegebenen Port
 */
public class CommunicationManager implements Runnable {
        //Port auf dem der Server gestartet werden soll
        private int serverPortNumber;

    /**
     * Konstruktor für diese Klasse
     * @param portnumber auf dem der Server gestartet werden soll
     */
    public CommunicationManager(int portnumber) {
        this.serverPortNumber = portnumber;
    }

    /**
     * diese Methode wird als ein eigener Thread gestartet
     * 1. eröffnet einen ServerSocket auf der serverPortNumber
     * 2. Wartet in einer Schleife immer wieder auf Verbindungsanfragen, nimmt diese an und empfängt Daten
     */
    @Override
    public void run() {
        try {
            //öffnet ServerSocket auf der angegebenen Portnummer und wartet auf Verbindung
            ServerSocket serverSocket = new ServerSocket(serverPortNumber);//Server-Seite
            System.out.println("Server auf Port:" + serverPortNumber);

            //Schleife, die immer wieder Verbindungen zulässt und verwaltet
            while (!serverSocket.isClosed()) {
                //Verbindungen werden akzeptiert und ein Socket erstellt
                Socket s = serverSocket.accept();

                //Der Input Stream von diesem Socket wird verwendet, um Daten empfangen
                InputStream is = s.getInputStream();
                //Durch die ProtocolEngine erhalten wir ein Objekt einer Klasse die PDUInterface implementiert (PDUMessage, PDUFile, ...)
                PDUInterface received = ProtocolEngine.deserialisiere(is);

                //Ausgabe der empfangenden Daten auf der Konsole mithilfe des ConsoleOutputManager
                ConsoleOutputManager.printReceivedData(received);
            }
        //Wenn ein Verbindungsfehler auftritt, wird eine RuntimeException geworfen
        } catch (IOException e) {
            throw new RuntimeException(e);
        //Wenn der Typ von empfangenden Daten nicht bekannt ist, wird das als Infonachricht ausgegeben
        } catch (IllegalStateException ex) {
            System.out.println("Der Typ von empfangenden Daten ist uns nicht bekannt.");
        }
    }

    /**
     * Klassenmethode die das Verbinden mit einem Server ermöglicht
     * @param sendeAnIP IP des Servers, mit dem man sich verbinden möchte
     * @param portNumber des Servers, auf dem man die Verbindung starten möchte
     * @return OutputStream des Sockets, der mit dem Server verbunden ist
     * @throws IOException wenn keine Verbindung möglich war
     */
    public static OutputStream connectToServer(String sendeAnIP, int portNumber) throws IOException {
        //Es wird versucht eine Verbindung zu einem anderen Gerät herzustellen mit der IP und Portnumber, die mitgegeben wurde
        Socket s = new Socket(sendeAnIP, portNumber);

        //Der OutputStream wird ermittelt und zurückgegeben, um Daten senden zu können
        OutputStream os = s.getOutputStream();
        return os;
    }
}
