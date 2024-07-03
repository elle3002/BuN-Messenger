package yourApp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleManager {
    public static void printReceivedData(PDUInterface receivedData) {

        String dataType = receivedData.getType();

        switch (dataType) {
            case "File":
                printReceivedPDUFile((PDUFile) receivedData);
                break;
            case "Message":
                printReceivedPDUMessage((PDUMessage) receivedData);
                break;
        }
    }

    private static void printReceivedPDUFile(PDUFile receivedData) {
        LocalDateTime arrivalTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = arrivalTime.format(formatter);

        NameIPLogic myNameIPLogic = MessengerLogic.getMyNameIPLogic();
        // Verwenden von StringBuilder, um die Konsolennachricht zu erstellen
        StringBuilder consoleMessage = new StringBuilder();
        consoleMessage.append(formattedTime)
                .append(": File von: ")
                .append(myNameIPLogic.getName(receivedData.getSenderIP()))
                .append(" --> Gespeichert unter: ")
                .append(receivedData.getFilePath() );

        System.out.println(consoleMessage.toString());
    }

    private static void printReceivedPDUMessage(PDUMessage receivedData) {
        LocalDateTime arrivalTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = arrivalTime.format(formatter);

        NameIPLogic myNameIPLogic = MessengerLogic.getMyNameIPLogic();
        // Verwenden von StringBuilder, um die Konsolennachricht zu erstellen
        StringBuilder consoleMessage = new StringBuilder();
        consoleMessage.append(formattedTime)
                .append(": Nachricht von: ")
                .append(myNameIPLogic.getName(receivedData.getSenderIP()))
                .append(" -> ")
                .append(receivedData.getMessage() );

        System.out.println(consoleMessage.toString());
    }

    public static void printSendMessageNotification(String[] parameter) {
        StringBuilder consoleMessage = new StringBuilder();
        consoleMessage.append("An ")
                .append(parameter[0])
                .append(" wurde gesendet: ")
                .append(parameter[1]);

        System.out.println(consoleMessage.toString());
    }

    public static void printSendFileNotification(String[] parameter) {
        StringBuilder consoleMessage = new StringBuilder();
        consoleMessage.append("An ")
                .append(parameter[0])
                .append(" wurde das File: ")
                .append(parameter[1])
                .append(" gesendet");

        System.out.println(consoleMessage.toString());
    }
}
