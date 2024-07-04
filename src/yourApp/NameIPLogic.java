package yourApp;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class NameIPLogic {

        private Map<String,String> IPList = new HashMap<>();


    private static String FILENAME = "ressources/IPData.txt";

    public String getIP(String name) throws NoIPBehindThisNameExeption {
        String IP = null;

        try {
           IPList = getNameAndIPFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

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
        String name = null;

        try {
            IPList = getNameAndIPFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(IPList.containsKey(IP)) {
            name = IPList.get(IP);
        } else {
            name = "Fehler!";
        }
        return name;
    }

    public void addNameAndIP(String name, String IP) throws IOException {
        IPList.put(IP, name);

        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FILENAME));
        outputStream.writeObject(IPList);
    }

    private Map<String, String> getNameAndIPFromFile() throws IOException, ClassNotFoundException {
        IPList = (HashMap<String, String>) new ObjectInputStream(new FileInputStream(FILENAME)).readObject();

        return IPList;
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

}
