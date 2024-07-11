package yourApp;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Die PDUFIle ist eine Klasse, die festlegt, welche Elemente beim Senden und Empfangen von Files vorhanden sein müssen
 */
public class PDUFile implements PDUInterface, Serializable {

    //benötigte Attribute
    private String TYPE = "File";
    private String fileEndung;
    private String senderIP;
    private String filePath;
    byte[] data;

    /**
     * Konstruktor, der die gesamte PDUFile initialisiert
     * @param ip senderIP
     * @param filePath Dateipfad des zu sendenen Files
     * @param Data Daten des Files
     */
    public PDUFile (String ip, String filePath, byte[] Data) {
        this.senderIP = ip;
        this.filePath = filePath;
        this.data = Data;
        //Ermitteln der FileEndung mittels FileManager
        this.fileEndung = FileManager.extractFileEndung(filePath);
    }

    /**
     * gibt den Typ zurück
     * @return TYPE
     */
    @Override
    public String getType() {
        return this.TYPE;
    }

    /**
     * gibt die IP des Senders zurück
     * @return senderIP
     */
    @Override
    public String getSenderIP() {
        return this.senderIP;
    }

    /**
     * Gibt den Dateipfad zurück, an dem das File gespeichert ist
     * @return filePath
     */
    public String getFilePath() {
        return this.filePath;
    }

    /**
     * Gibt die FileData zurück
     * @return data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Gibt die Fileendung zurück
     * @return fileEndung
     */
    public String getFileEndung() {return fileEndung; }
}