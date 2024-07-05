package yourApp;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class ConsoleOutputManager {


    public static void printReceivedData(PDUInterface received) {

        LocalDateTime arrivalTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = arrivalTime.format(formatter);

        System.out.println("Arrival Time: " + formattedTime);
        Map<String,String> IPList = null;
        try {
            IPList = NameIPLogic.getNameAndIPFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String nameOfIP;

        if (IPList.containsKey(received.getSenderIP())) {
            nameOfIP = IPList.get(received.getSenderIP());
        } else{
            nameOfIP = received.getSenderIP();
        }



        switch (received.getType()) {

            case "File":

                PDUFile receivedFile = (PDUFile) received;
                StringBuilder outputString = new StringBuilder();
                outputString.append(formattedTime)
                        .append(": File von: ")
                        .append(nameOfIP)
                        .append(" --> Gespeichert unter: ")
                        .append(receivedFile.getFilePath());
                System.out.println(outputString);
                break;

            case "Message":

                PDUMessage receivedMessage = (PDUMessage) received;
                StringBuilder outputMessage = new StringBuilder();
                outputMessage.append(formattedTime)
                        .append(": Message von: ")
                        .append(nameOfIP)
                        .append(": ")
                        .append(receivedMessage.getMessage());
                System.out.println(outputMessage);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + received.getType());
        }
    }
}
