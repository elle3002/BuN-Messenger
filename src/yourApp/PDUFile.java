package yourApp;

public class PDUFile implements PDUInterface{

    private String TYPE = "File";
    private String senderIP;
    private String filePath;

    public PDUFile (String ip, String filePath) {
        this.senderIP = ip;
        this.filePath = filePath;
    }

    @Override
    public String getType() {
        return this.TYPE;
    }

    @Override
    public String getSenderIP() {
        return this.senderIP;
    }

    public String getFilePath() {
        return this.filePath;
    }
}