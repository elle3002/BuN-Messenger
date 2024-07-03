package yourApp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommunicationManager implements Runnable {
    private static final int DEFAULTPORT = 3333;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);


    public static OutputStream connectToSendData(String sendeAnIP) throws IOException {

        Socket connectToServer = new Socket(sendeAnIP, DEFAULTPORT);

        return connectToServer.getOutputStream();
    }

    @Override
    public void run() {
        try {
            ServerSocket myServerSocket = new ServerSocket(DEFAULTPORT);

            while (!myServerSocket.isClosed()) {
                Socket clientSocket = myServerSocket.accept();

                threadPool.submit(new Communication(clientSocket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
