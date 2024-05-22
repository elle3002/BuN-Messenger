package yourApp;

public class PDUMessage implements PDUInterface {
    private String TYPE = "Message";
    private String senderIP;
    private String message;

    public PDUMessage (String ip, String message) {
        this.senderIP = ip;
        this.message = message;
    }


    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getSenderIP() {
        return senderIP;
    }

    public String getMessage() {
        return this.message;
    }

}
