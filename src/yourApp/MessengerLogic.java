package yourApp;

import java.io.IOException;

/**
 *
 */
public class MessengerLogic {

    static NameIPLogic nameIPLogic = new NameIPLogic();

    /**
     * Diese Methode weist einem Namen eine IP zu
     * Diese werden in IP-Adressen.txt abegelegt
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
     *              [0] = Name der IP an die die Nachricht gesendet werden soll
     *              [1] = Nachricht die gesendet werden soll
     */
    public static void sendMessage (String[] parameter) throws IOException {
        try {
            //IP hinter Name erfahren
            String sendeAnIP = nameIPLogic.getIP(parameter[0]);

            //PDUMessage erstellen
            PDUMessage myMessage = new PDUMessage("my IP", parameter[1]);

            // TODO: Verbindung zu sendeAnIP herstellen und myMessage serialisieren!

            System.out.println("An: " + parameter[0] + " (" + sendeAnIP + ") wurde gesendet: " + parameter[1]);
        } catch (NoIPBehindThisNameExeption e) {
            System.out.println("Unter diesem Namen ist keine IP gespeichert!");
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
            PDUFile myFile = new PDUFile("my IP", parameter[1], imageData);

            // TODO: Verbindung zu sendeAnIP herstellen und myFile serialisieren!

            System.out.println("An: " + parameter[0] + " (" + sendeAnIP + ") wurde gesendet: " + parameter[1]);
        } catch (NoIPBehindThisNameExeption e) {
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
}
