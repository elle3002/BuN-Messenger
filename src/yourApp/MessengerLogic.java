package yourApp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Stellt die Logig hinter dem Messanger dar
 */
public class MessengerLogic {

    //Die NameIPLogic der Klasse
    static NameIPLogic nameIPLogic = new NameIPLogic();
    //die eigene IP
    private static String myIP = "unknown";

    /**
     * Diese Methode weist einem Namen eine IP in der NameIPLogic zu
     * Diese werden in IPData.txt abegelegt
     * @param parameter beinhaltet die benötigten Parameter
     *              [0] = Name der hinzugefügt werden soll
     *              [1] = IP die dem Namen zugewiesen werden soll
     */
    public static void add (String[] parameter) {

        try {
            nameIPLogic.addNameAndIP(parameter[0], parameter[1]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("IP: " + parameter[1] + " wurde: " + parameter[0] + " zugewiesen");
    }

    /**
     * Diese Methode sendet an eine angegebene Person eine Nachricht
     * @param parameter beinhaltet die benötigten Parameter
     *              [0] = Name der IP, an die die Nachricht gesendet werden soll
 *                  [1] = Port an den gesendet werden soll
     *              [2] = Nachricht die gesendet werden soll
     */
    public static void sendMessage (String[] parameter) throws IOException {
        try {
            // IP hinter Name erfahren
            String sendeAnIP = nameIPLogic.getIP(parameter[0]);

            // Portnummer
            int portnumber = Integer.parseInt(parameter[1]);

            // PDUMessage erstellen
            PDUMessage myMessage = new PDUMessage(printMyIP(), parameter[2]);

            // Verbindung zu sendeAnIP herstellen und myMessage serialisieren!
            OutputStream outputStream = CommunicationManager.connectToServer(sendeAnIP, portnumber);
            ProtocolEngine.serialisiereMessage(outputStream, myMessage);

            //Connection beenden
            outputStream.close();

            System.out.println("An: " + parameter[0] + " (" + sendeAnIP + ") wurde gesendet: " + parameter[1]);

        //Wenn der Name unbekannt ist, wird diese Exception behandelt, indem folgendes auf der Konsole ausgegeben wird
        } catch (NoIPBehindThisNameException e) {
            System.out.println("Unter diesem Namen ist keine IP gespeichert!");
        }
    }

    /**
     * Diese Methode sendet an eine angegebene Person ein File
     * @param parameter beinhaltet die benötigten Parameter
     *              [0] = Name der IP an die die Nachricht gesendet werden soll
 *                  [1] = Portnummer, an die gesendet werden soll
     *              [2] = Filepath des Files das gesendet werden soll
     */
    public static void sendFile(String[] parameter) {
        try {
            //IP hinter Name erfahren
            String sendeAnIP = nameIPLogic.getIP(parameter[0]);

            //Portnummer
            int portnumber = Integer.parseInt(parameter[1]);

            //PDUFile erstellen
            byte[] imageData = FileManager.readFile(parameter[2]);
            PDUFile myFile = new PDUFile(printMyIP(), parameter[2], imageData);

            //Verbindung zu sendeAnIP herstellen und myFile serialisieren!
            OutputStream os = CommunicationManager.connectToServer(sendeAnIP, portnumber);
            ProtocolEngine.serialisiereFile(os, myFile);

            //Connection beenden
            os.close();

            //Textausgabe
            System.out.println("An: " + parameter[0] + " (" + sendeAnIP + ") wurde gesendet: " + parameter[2]);

        //Wenn der Name unbekannt ist, wird diese Exception behandelt, indem folgendes auf der Konsole ausgegeben wird
        } catch (NoIPBehindThisNameException e) {
            System.out.println("Unter diesem Namen ist keine IP gespeichert!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gibt alle gespeicherten Namen und deren IPs aus
     */
    public static void show () {
        nameIPLogic.printAllEntrys();
    }

    /**
     * ermittelt und gibt die eigene IP zurück
     * @return myIP
     */
    public static String printMyIP() {
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
                            return inetAddress.getHostAddress();
                        }
                    } else if (!inetAddress.isLoopbackAddress() && inetAddress.isSiteLocalAddress()){ // für Windows-OS
                        return inetAddress.getHostAddress(); // kehre zurück nachdem IP-Adresse gefunden wurde
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Die öffentliche IP-Addresse kann nicht ermittelt werden: " + e.getMessage());
        }
        return myIP;
    }
}
