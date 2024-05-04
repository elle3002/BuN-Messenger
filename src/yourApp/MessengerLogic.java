package yourApp;

/**
 *
 */
public class MessengerLogic {

    /**
     * Diese Methode weist einem Namen eine IP zu
     * Diese werden in IP-Adressen.txt abegelegt
     * @param parameter beinhaltet die benötigten Parameter
     *              [0] = Name der hinzugefügt werden soll
     *              [1] = IP die dem Namen zugewiesen werden soll
     */
    public static void add (String[] parameter) {
        // TODO: schreibe in name + IP in eine file

        System.out.println("IP: " + parameter[1] + " wurde: " + parameter[0] + " zugewiesen");
    }

    /**
     * Diese Methode sendet an eine angegebene Person eine Nachricht
     * @param parameter beinhaltet die benötigten Parameter
     *              [0] = Name der IP an die die Nachricht gesendet werden soll
     *              [1] = Nachricht die gesendet werden soll
     */
    public static void sendMessage (String[] parameter) {
        // TODO: sende parameter[1] an die IP des Namens parameter[0]

        System.out.println("Ich --> " + parameter[0] + ": " + parameter[1]);
    }

    /**
     * Diese Methode sendet an eine angegebene Person ein File
     * @param parameter beinhaltet die benötigten Parameter
     *              [0] = Name der IP an die die Nachricht gesendet werden soll
     *              [1] = Filepath des Files das gesendet werden soll
     */
    public static void sendFile(String[] parameter) {
        // TODO: sende das File in parameter[1] an die IP des Namens parameter[0]

        System.out.println("Ich --> " + parameter[0] + ": File: " + parameter[1] + " gesendet");
    }

    /**
     * Gibt alle gespeicherten Namen und deren IPs aus
     */
    public static void show () {
        // TODO: alle Namen und deren IPs ausgeben
    }
}
