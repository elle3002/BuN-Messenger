package yourApp;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class NameIPLogic {

        private Map<String,String> IPList = new HashMap<>();

        private static String FILENAME = "ressources/IPData.txt";

    public NameIPLogic() {
        try {
            this.IPList = getNameAndIPFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getIP(String name) throws NoIPBehindThisNameExeption {

        String IP = null;

        if (this.IPList.containsValue(name)) {
            IP = findKeyByValue(this.IPList, name);
        }
        if (IP == null) {
            throw new NoIPBehindThisNameExeption();
        }
        return IP;
    }

    private static String findKeyByValue(Map<String, String> map, String value) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String getName(String IP) {
        String name;

        if(this.IPList.containsKey(IP)) {
            name = this.IPList.get(IP);
        } else {
            name = "Fehler!";
        }
        return name;
    }

    public void addNameAndIP(String name, String IP) throws IOException {
        this.IPList.put(IP, name);

        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FILENAME));
        outputStream.writeObject(this.IPList);

        try {
            Map<String, String> listetest = getNameAndIPFromFile();
            System.out.println(listetest.toString());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static Map<String, String> getNameAndIPFromFile() throws IOException, ClassNotFoundException {
        Map<String,String> IPList = null;
        try {
            File file = new File(FILENAME);

            // Falls die Datei nicht existiert, gib null zurück
            if (!file.exists()) {
                IPList = new HashMap<>();
            } else {
                IPList = (HashMap<String, String>) new ObjectInputStream(new FileInputStream(file)).readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Fehler beim Lesen der IP-Daten: " + e.getMessage());
        }
        return IPList;
    }

    public void printAllEntrys() {
        if (this.IPList.size() == 0) {
            System.out.println("Es gibt noch keine Einträge!");
        } else {
            for (Map.Entry<String, String> entry : IPList.entrySet()) {
                System.out.println("Name: " + entry.getValue() + ", IP: " + entry.getKey());
            }
        }
        try {
            System.out.println(getNameAndIPFromFile().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
