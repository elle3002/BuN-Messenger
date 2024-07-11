package yourApp;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Ist für das Ausgeben von empfangenen Daten zuständig mithilfe von Klassenmethoden
 */
public class ConsoleOutputManager {

    /**
     * Klassenmethode, die Daten auf die Konsole ausgibt
     * @param received ist ein Objekt einer Klasse das das Interface PDUInterface implementiert (PDUMessage, PDUFile, ...), welches ausgegeben werden soll
     */
    public static void printReceivedData(PDUInterface received) {

        //Aktuelle Zeit ermitteln
        LocalDateTime arrivalTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = arrivalTime.format(formatter);

    //Überprüfen, ob die IP des Senders uns bereits bekannt ist. Wenn ja wird statt der senderIP der Name ausgegeben, den wir uns gespeichert haben
        //die aktulle IPListe ermitteln
        Map<String,String> IPList = null;
        IPList = NameIPLogic.getNameAndIPFromFile();

        //Überprüfen, ob die IP des Senders bekannt ist
        String nameOfIP;
        if (IPList.containsKey(received.getSenderIP())) {
            //Wenn die IP des Senders eingetragen ist, wird der Name der IP gespeichert
            nameOfIP = IPList.get(received.getSenderIP());
        } else{
            //Wenn die IP des Senders nicht eingetragen ist, wird die IP als Name gespeichert
            nameOfIP = received.getSenderIP();
        }

        //Je nach Typ werden unsere empfangenden Daten ausgegeben
        switch (received.getType()) {

            case "File":
                //Wenn es sich um ein File handelt, werden die Daten in ein PDUFile gecastet
                PDUFile receivedFile = (PDUFile) received;

                //Die Nachticht, die ausgegeben werden soll, wird erstellt
                StringBuilder outputString = new StringBuilder();
                outputString.append(formattedTime)
                        .append(": File von: ")
                        .append(nameOfIP)
                        .append(" --> Gespeichert unter: ")
                        .append(receivedFile.getFilePath());

                //Die Nachricht wird auf der Konsole ausgegeben
                System.out.println(outputString);
                break;

            case "Message":
                //Wenn es sich um eine Message handelt, werden die Daten in ein PDUMessage gecastet
                PDUMessage receivedMessage = (PDUMessage) received;

                //Die Nachricht, die ausgegeben werden soll, wird erstellt
                StringBuilder outputMessage = new StringBuilder();
                outputMessage.append(formattedTime)
                        .append(": Message von: ")
                        .append(nameOfIP)
                        .append(": ")
                        .append(receivedMessage.getMessage());

                //Die Nachricht wird auf der Konsole ausgegeben
                System.out.println(outputMessage);
                break;
            default:
                //wenn es sich um einen anderen Typ handelt, wird eine IllegalStateException geworfen
                throw new IllegalStateException("Unexpected value: " + received.getType());
        }
    }
}
