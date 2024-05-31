package yourApp;

import java.io.*;

/**
 * serialisieren und deserialisieren von Messages und Files
 */
public class ProtocolEngine {

    //TODO: ChatProtokoll: - ca. Name, Zeit, ip, daten;

    public static void serialisiereMessage(OutputStream os, PDUMessage message) throws IOException {

        DataOutputStream daos = new DataOutputStream(os);
        daos.writeUTF(message.getType());
        daos.writeUTF(message.getSenderIP());
        daos.writeUTF(message.getMessage());
    }

    public static void serialisiereFile(OutputStream os, PDUFile file) throws IOException {

        DataOutputStream daos = new DataOutputStream(os);

        daos.writeUTF(file.getType());
        daos.writeUTF(file.getSenderIP());

        //TODO FileOutputStream checken



    }

    public static PDUInterface deserialisiere (InputStream is) throws IOException {

        DataInputStream dais = new DataInputStream(is);

        String type = dais.readUTF();

        PDUInterface result;

        switch (type) {
            case "Message": result = deserialisiereMessage(dais);
                            break;
            case "File": result = deserialisiereFile(dais);
                        break;

            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return result;
    }


    public static PDUMessage deserialisiereMessage(DataInputStream dais) throws IOException {

        String senderIP = dais.readUTF();
        String message = dais.readUTF();
        return new PDUMessage(senderIP, message);
    }

    public static PDUFile deserialisiereFile(DataInputStream dais) throws IOException {

        String senderIP = dais.readUTF();

        //TODO FileInputStream checken

        return new PDUFile(senderIP, "Filepath");
    }
}
