package yourApp;

public interface PDUInterface {

    /**
     * gibt den Typ der PDU wieder
     * @return String Typ der PDU
     */
    String getType();

    /**
     * gibt die IP des Senders wieder
     * @return Sting IP des Senders
     */
    String getSenderIP();

}
