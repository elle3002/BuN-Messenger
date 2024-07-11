package yourApp;

import java.io.*;


/**
 * serialisieren und deserialisieren von Messages und Files
 */
public class ProtocolEngine {

    /**
     * Gibt an wie eine PDUMessage serialisiert wird und sendet eine solche
     * @param os = OutputStream, in den die PDUMessage geschrieben werden soll (sendet)
     * @param message die zu sendene PDUMessage
     * @throws IOException bei Verbindungsfehler
     */
    public static void serialisiereMessage(OutputStream os, PDUMessage message) throws IOException {
        //erstellen eines DataOutputStreams zum einfacheren senden von Strings
        DataOutputStream daos = new DataOutputStream(os);

        //senden der PDUMessage
        daos.writeUTF(message.getType());
        daos.writeUTF(message.getSenderIP());
        daos.writeUTF(message.getMessage());
    }

    /**
     * Gibt an wie ein PDUFile serialisiert wird und sendet eine solche
     * @param os = OutputStream, in den die PDUMessage geschrieben werden soll (sendet)
     * @param file die zu sendene PDUFile
     * @throws IOException bei Verbindungsfehler
     */
    public static void serialisiereFile(OutputStream os, PDUFile file) throws IOException {
        //erstellen eines DataOutputStreams zum einfacheren senden von Strings
        DataOutputStream daos = new DataOutputStream(os);

        //senden der PDUFile
        daos.writeUTF(file.getType());
        daos.writeUTF(file.getSenderIP());
        daos.writeUTF(file.getFileEndung());
        daos.writeInt(file.getData().length);
        daos.write(file.getData());
    }

    /**
     * Diese Methode erhält einen InputStream und liest daraus ein Objekt einer Klasse aus, welches das PDUInterface implementiert, aus (PDUMessage, PDUFile, ...)
     * @param is Der InputStream aus dem gelesen werden soll
     * @return Objekt einer Klasse, die das PDUInterace implementiert (PDUMessage, PDUFIle, ...)
     * @throws IOException bei Verbindungsfehler
     */
    public static PDUInterface deserialisiere (InputStream is) throws IOException {
        //erstellen eines DataInputStream und einfacheren Lesen von Strings
        DataInputStream dais = new DataInputStream(is);

        //Lesen des Typs
        String type = dais.readUTF();

        //Je nach Typ die deserialisier-Methode aufrufen
        PDUInterface result;
        switch (type) {
            case "Message": result = deserialisiereMessage(dais);
                            break;
            case "File": result = deserialisiereFile(dais);
                        break;
            //Bei unerwartetem Typ wird eine IllegalStateExeption geworfen
            default:
                throw new IllegalStateException("Unerwareter Typ beim Lesen: " + type);
        }
        return result;
    }

    /**
     * Deserialisiert eine PDUMessage aus einem DataInputStream
     * @param dais DataInputStream aus dem gelesen werden soll
     * @return eine neu erstelle PDUMessage
     * @throws IOException bei Verbindungsfehler
     */
    public static PDUMessage deserialisiereMessage(DataInputStream dais) throws IOException {
        //auslesen der einzelnen Attribute
        String senderIP = dais.readUTF();
        String message = dais.readUTF();

        //erstellen und zurückgeben der empfangenen PDUMessage
        return new PDUMessage(senderIP, message);
    }

    /**
     * Deserialisiert eine PDUFile aus einem DataInputStream
     * @param dais DataInputStream aus dem gelesen werden soll
     * @return eine neu erstelle PDUFile
     * @throws IOException bei Verbindungsfehler
     */
    public static PDUFile deserialisiereFile(DataInputStream dais) throws IOException {
        //auslesen der einzelnen Attribute
        String senderIP = dais.readUTF();
        String fileEndung = dais.readUTF();
        int length = dais.readInt();
        byte[] data = new byte[length];
        dais.readFully(data);

        //Speichern des Files mithilfe des FileManagers
        String filepath = FileManager.saveFile(senderIP, data, fileEndung);

        //erstellen und zurückgeben der empfangenden PDUFile
        return new PDUFile(senderIP, filepath, data);
    }
}
