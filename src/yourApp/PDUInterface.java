package yourApp;

/**
 * Interface für alle PDUs, die mit unserem Chat verschickt werden wollen
 */
public interface PDUInterface {

    /**
     * gibt den Typ der PDU wieder
     * @return String TYPE der PDU
     */
    String getType();

    /**
     * gibt die IP des Senders wieder
     * @return Sting IP des Senders
     */
    String getSenderIP();

}
