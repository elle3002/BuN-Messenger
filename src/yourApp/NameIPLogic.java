package yourApp;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * ist für die Gesamte Logig hinter dem "DNS-Service"
 * speichert Namen hinter IPs in einer Liste und in einer Datei
 */
public class NameIPLogic {

        private Map<String,String> IPList;

        //Ort der Datei, in der gespeichert werden soll
        private static String FILENAME = "ressources/IPData.txt";

    /**
     * Konstruktor der Klasse
     */
    public NameIPLogic() {
        //Befüllt die Map IPList mit den Daten aus der Datei IPData.txt
        this.IPList = getNameAndIPFromFile();
    }

    /**
     * Gibt die IP von dem gegebenen Namen
     * @param name deren IP man ermitteln möchte
     * @return IP des name
     * @throws NoIPBehindThisNameException Wenn der Name name unbekannt ist
     */
    public String getIP(String name) throws NoIPBehindThisNameException {

        String IP = null;

        //Prüft ob der name in der Map vorhanden ist
        if (this.IPList.containsValue(name)) {
            //Falls ja wird findKeyByValue aufgerufen, um die IP zu ermitteln
            IP = findKeyByValue(this.IPList, name);
        }
        // wenn die IP nicht verändert wurde, wird die NoIPBehindThisNameException geworfen
        if (IP == null) {
            throw new NoIPBehindThisNameException();
        }
        return IP;
    }

    /**
     * ist dafür da die IP (key) eines Namens (value) zu finden
     * @param map die Liste, in der gesucht werden soll
     * @param value der Name, deren IP man haben möchte
     * @return IP des namens
     */
    private static String findKeyByValue(Map<String, String> map, String value) {
        //Geht durch jeden Entry der Liste
        for (Map.Entry<String, String> entry : map.entrySet()) {
            //Prüft ob das Value der gesuchte Name ist
            if (entry.getValue().equals(value)) {
                //Falls ja, wird der Key (IP) dieses Entrys zurückgegeben
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Fügt ein neues Name-IP-Paar der Liste hinzu und speichert es in dem File
     * @param name der gespeichert werden soll (value)
     * @param IP die gespeichert werden soll (key)
     * @throws IOException
     */
    public void addNameAndIP(String name, String IP) throws IOException {
        //Fügt es der Liste hinzu
        this.IPList.put(IP, name);

        //Schreibt es mit einem ObjectOutputstream in ein File mithilfe eines FileOutputstreams
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FILENAME));
        outputStream.writeObject(this.IPList);
    }

    /**
     * Liest aus dem File IPData.txt die aktuelle Liste aus
     * @return eine HashMap mit allen IPs und den dazugehörigen Namen
     */
    public static Map<String, String> getNameAndIPFromFile() {
        Map<String,String> IPList = null;
        try {
            File file = new File(FILENAME);

            // Falls die Datei nicht existiert, wird eine neue leere HashMap erstellt
            if (!file.exists()) {
                IPList = new HashMap<>();
            } else {
                //IPList wird bestückt durch einen FileInputStream, dessen Daten durch einen ObjectInputStream und dem Cast zur HashMap werden
                IPList = (HashMap<String, String>) new ObjectInputStream(new FileInputStream(file)).readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Fehler beim Lesen der IP-Daten: " + e.getMessage());
        }
        return IPList;
    }

    /**
     * Gibt alle Entrys der Liste auf die Console aus
     */
    public void printAllEntrys() {
        //Wenn die Liste leer ist
        if (this.IPList.size() == 0) {
            System.out.println("Es gibt noch keine Einträge!");
        } else {
            //For Loop geht durch jeden Entry und gibt ihn nacheinander aus
            for (Map.Entry<String, String> entry : IPList.entrySet()) {
                System.out.println("Name: " + entry.getValue() + ", IP: " + entry.getKey());
            }
        }
    }

}
