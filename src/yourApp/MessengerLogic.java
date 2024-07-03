package yourApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.Enumeration;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 *
 */
public class MessengerLogic {
    private static final NameIPLogic nameIPLogic = new NameIPLogic();
    private static String myIP = "unknown";

    static {
        try {
            myIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println("Deine IP-Adresse kann nicht ermittelt werden!");
        }
    }

    /**
     * Diese Methode weist einem Namen eine IP zu
     * Diese werden in IP-Adressen.txt abegelegt
     * @param parameter beinhaltet die benötigten Parameter
     *              [0] = Name der hinzugefügt werden soll
     *              [1] = IP die dem Namen zugewiesen werden soll
     */
    public static void add (String[] parameter) throws IOException {

        nameIPLogic.addNameAndIP(parameter[0], parameter[1]);

        System.out.println("IP: " + parameter[1] + " wurde: " + parameter[0] + " zugewiesen");
    }

    /**
     * Diese Methode sendet an eine angegebene Person eine Nachricht
     * @param parameter beinhaltet die benötigten Parameter
     *              [0] = Name der IP an die die Nachricht gesendet werden soll
     *              [1] = Nachricht die gesendet werden soll
     */
    public static void sendMessage (String[] parameter) throws IOException {
        try {
            //IP hinter Name erfahren
            String sendeAnIP = nameIPLogic.getIP(parameter[0]);

            //PDUMessage erstellen
            PDUMessage myMessage = new PDUMessage(myIP, parameter[1]);

            //Verbindung zu sendeAnIP herstellen und myMessage serialisieren!

            OutputStream os = CommunicationManager.connectToSendData(sendeAnIP);
            ProtocolEngine.serialisiereMessage(os, myMessage);
            os.close();

            //Auf die Konsole ausgeben
            ConsoleManager.printSendMessageNotification(parameter);

        } catch (NoIPBehindThisNameExeption e) {
            System.out.println("Unter diesem Namen ist keine IP gespeichert!");
        } catch (IOException e) {
            System.out.println("Es ist ein Fehler aufgetreten, es ist wahrscheinlich, das die Nachricht nicht angekommen ist!");
        }
    }

    /**
     * Diese Methode sendet an eine angegebene Person ein File
     * @param parameter beinhaltet die benötigten Parameter
     *              [0] = Name der IP an die die Nachricht gesendet werden soll
     *              [1] = Filepath des Files das gesendet werden soll
     */
    public static void sendFile(String[] parameter) {
        try {
            //IP hinter Name erfahren
            String sendeAnIP = nameIPLogic.getIP(parameter[0]);

            //PDUMessage erstellen
            byte[] imageData = FileManager.readFile(parameter[1]);
            PDUFile myFile = new PDUFile(myIP, parameter[1], imageData);

            //Verbindung zu sendeAnIP herstellen und myFile serialisieren!
            OutputStream os = CommunicationManager.connectToSendData(sendeAnIP);
            ProtocolEngine.serialisiereFile(os, myFile);
            os.close();

            //Auf die Console ausgeben
            ConsoleManager.printSendFileNotification(parameter);

        } catch (NoIPBehindThisNameExeption e) {
            System.out.println("Unter diesem Namen ist keine IP gespeichert!");
        } catch (IOException e) {
            System.out.println("Es ist ein Fehler aufgetreten, es ist wahrscheinlich, das die Nachricht nicht angekommen ist!");
        }
    }

    /**
     * Gibt alle gespeicherten Namen und deren IPs aus
     */
    public static void show () {
        nameIPLogic.printAllEntrys();
    }

    public static NameIPLogic getMyNameIPLogic() {
        return nameIPLogic;
    }

    public static void printMyIP() { // getLocalIpAdress
        System.out.println("Meine aktuelle IP-Adresse ist: " + myIP);
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); // ruft alle Netzwerkschnitstellen auf
            while(interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement(); // durchläuft alle Schnitstellen und speichert diese
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses(); // gibt alle IP-Adr. von den Schnitst. in eine Enum. aller Objekte
                while(addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement(); // durchläuft die jede IP-Adr.
                    // Filtert loopback und non-site Adressen raus
                    if (!inetAddress.isLoopbackAddress() && inetAddress.isSiteLocalAddress()) {
                        // für Linux-OS
                        if (networkInterface.getName().equals("wlp2s0")) {
                            System.out.println("Meine lokale IP-Adresse ist, schicke diese an deinem Chatpartner: " + inetAddress.getHostAddress());
                            return;
                        }
                    } else if (!inetAddress.isLoopbackAddress() && inetAddress.isSiteLocalAddress()){ // für Windows-OS
                        System.out.println("Meine locale IP-Adresse ist, schicke diese an deinem Chatpartner:: " + inetAddress.getHostAddress());
                        return; // kehre zurück nachdem IP-Adresse gefunden wurde
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Die öffentliche IP-Adresse kann nicht ermittelt werden: " + e.getMessage());
        }
    }
}
