package yourApp;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Communication implements Runnable {

    private Socket clientSocket;
    public Communication(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            InputStream is = clientSocket.getInputStream();
            PDUInterface receivedData =  ProtocolEngine.deserialisiere(is);
            ConsoleManager.printReceivedData(receivedData);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
