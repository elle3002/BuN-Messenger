package yourApp;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ConsoleOutputManager {


    public static void printReceivedData(PDUInterface received) {

        LocalDateTime arrivalTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = arrivalTime.format(formatter);

        switch (received.getType()) {
            case "File":
                PDUFile receivedFile = (PDUFile) received;
                StringBuilder outputString = new StringBuilder();
                outputString.append(formattedTime)
                        .append(": File von: ")
                        .append(receivedFile.getSenderIP())
                        .append(" --> Gespeichert unter: ")
                        .append(receivedFile.getFilePath());
                System.out.println(outputString);
                break;
            case "Message":
                PDUMessage receivedMessage = (PDUMessage) received;
                StringBuilder outputMessage = new StringBuilder();
                outputMessage.append(formattedTime)
                        .append(": Message von: ")
                        .append(receivedMessage.getSenderIP())
                        .append(": ")
                        .append(receivedMessage.getMessage());
                System.out.println(outputMessage);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + received.getType());
        }
    }
}
