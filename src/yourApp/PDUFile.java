package yourApp;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PDUFile implements PDUInterface, Serializable {

    private String TYPE = "File";
    private String fileEndung;
    private String senderIP;
    private String filePath;

    byte[] data;

    public PDUFile (String ip, String filePath, byte[] Data) {
        this.senderIP = ip;
        this.filePath = filePath;
        this.data = Data;
        this.fileEndung = FileManager.extractFileEndung(filePath);
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

    public byte[] getData() {
        return data;
    }

    public String getFileEndung() {return fileEndung; }
}