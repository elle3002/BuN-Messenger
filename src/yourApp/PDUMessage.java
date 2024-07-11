package yourApp;

/**
 * Die PDUMessage ist eine Klasse, die festlegt, welche Elemente beim Senden und Empfangen von Nachrichten vorhanden sein müssen
 */
public class PDUMessage implements PDUInterface {

    //benötigte Attribute
    private String TYPE = "Message";
    private String senderIP;
    //private String name;
    private String message;

    /**
     * Konstruktor, der die gesamte PDUMessage initialisiert
     * @param ip senderIP
     * @param message Nachricht, die gesendet/empfangen wurde
     */
    public PDUMessage (String ip, String message) {
        this.senderIP = ip;
        this.message = message;
    }

    /**
     * Gibt den Typ zurück
     * @return TYPE
     */
    @Override
    public String getType() {
        return TYPE;
    }

    /**
     * Gibt die IP des Senders der Nachricht zurück
     * @return senderIP
     */
    @Override
    public String getSenderIP() {
        return senderIP;
    }

    /**
     * gibt die Nachricht zurück die gesendet oder empfangen wurde
     * @return message
     */
    public String getMessage() {
        return this.message;
    }

}
