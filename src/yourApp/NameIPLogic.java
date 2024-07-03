package yourApp;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class NameIPLogic {

    private Map<String,String> IPList = new HashMap<>();
    private static String FILENAME = "/home/goosey/Downloads/BuN-Messenger(Lokal)/ressources/IPData.txt";

    public NameIPLogic () {
        this.getNameAndIPFromFile();
    }

    public String getIP(String name) throws NoIPBehindThisNameExeption {
        String IP = null;

        if (IPList.containsValue(name)) {
            IP = findKeyByValue(IPList, name);
        }
        if (IP == null) {
            throw new NoIPBehindThisNameExeption();
        }
        return IP;
    }

    public static String findKeyByValue(Map<String, String> map, String value) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String getName(String IP) {
        String name;

        if(IPList.containsKey(IP)) {
            name = IPList.get(IP);
        } else {
            name = IP;
        }
        return name;
    }

    public void addNameAndIP(String name, String IP) throws IOException {
        IPList.put(IP, name);
        this.saveIPList();
    }

    private void getNameAndIPFromFile() {
        try {
            File file = new File(FILENAME);

            // Falls die Datei nicht existiert, gib null zurück
            if (!file.exists()) {
                this.IPList = new HashMap<>();
            } else {
                this.IPList = (HashMap<String, String>) new ObjectInputStream(new FileInputStream(file)).readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Fehler beim Lesen der IP-Daten: " + e.getMessage());
        }
    }

    public void printAllEntrys() {
        if (IPList.size() == 0) {
            System.out.println("Es gibt noch keine Einträge!");
        } else {
            for (Map.Entry<String, String> entry : IPList.entrySet()) {
                System.out.println("Name: " + entry.getValue() + ", IP: " + entry.getKey());
            }
        }
    }

    public synchronized void saveIPList() {
        try {
            File file = new File(FILENAME);

            // Falls die Datei nicht existiert, erstelle sie
            if (!file.exists()) {
                file.createNewFile();
            }

            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
                outputStream.writeObject(IPList);
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern der IP-Daten: " + e.getMessage());
        }
    }

}
