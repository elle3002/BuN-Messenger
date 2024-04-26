package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class StreamConnectionFactory {
    private final int port;
    private final int byteFailureRate;
    private final boolean gauss;
    private ServerSocket serverSocket;
    private List<StreamConnectionFactoryListener> connectionCreatedListeners = new ArrayList<>();
    private ConnectionAttemptsThread connectionAttemptThread = null;

    public StreamConnectionFactory(int port, int bitFailureRate, boolean gauss) throws IOException {
        this.port = port;

        if(bitFailureRate < 0) throw new IOException("failure rate cannot be a negative number");
        this.byteFailureRate = bitFailureRate;
        this.gauss = gauss;
    }
    public StreamConnectionFactory(int port) throws IOException {
        this(port, 0, true);
    }

    public void addConnectionListener(StreamConnectionFactoryListener connectionCreatedListener) {
        this.connectionCreatedListeners.add(connectionCreatedListener);
    }

    private ServerSocket getServerSocket() throws IOException {
        if(this.serverSocket == null) {
            this.serverSocket = new ServerSocket(this.port);
            Log.writeLog(this, "server socket created");
        }

        return this.serverSocket;
    }

    private AcceptConnectionRequestThreads acceptConnectionRequestThread = null;

    /**
     * close port - other peers can no longer connect.
     * @exception IOException cannot close existing server socket - weired.
     */
    public void stopAcceptingConnectionRequests() throws IOException {
        this.acceptConnectionRequestThread = null;
        if(this.serverSocket != null) {
            this.serverSocket.close();
            this.serverSocket = null;
        }
    }

    /**
     * Provide an open port.
     * @param multiple true - port remains open after peer connected / false - port is closed
     * @throws IOException cannot create server port - other potential problems are handled
     */
    public void acceptConnectionRequests(boolean multiple) throws IOException {
        if(this.acceptConnectionRequestThread != null) {
            Log.writeLog(this, "already accepting connection requests");
            return;
        }

        this.acceptConnectionRequestThread = new AcceptConnectionRequestThreads(multiple);
        this.acceptConnectionRequestThread.start();
    }

    private class AcceptConnectionRequestThreads extends Thread {
        private boolean again;

        public AcceptConnectionRequestThreads(boolean again) {
            this.again = again;
        }

        @Override
        public void run() {
            Socket socket = null;
            try {
                do {
                    socket = StreamConnectionFactory.this.getServerSocket().accept();
                    InputStream is = socket.getInputStream();
                    OutputStream os = socket.getOutputStream();
                    String remoteAddress = getRemoteAddress(socket);
                    Log.writeLog(this, "connection request served: socket created - tell listener(s)");
                    StreamConnectionFactory.this.notifyConnectionCreatedListeners(is, os, true, remoteAddress);
                } while (again);
                StreamConnectionFactory.this.stopAcceptingConnectionRequests();
            } catch (IOException e) {
                Log.writeLogErr(this, e.getLocalizedMessage());
                again = false;
            }
        }
    }

    public static final String getRemoteAddress(Socket connectedSocket) throws IOException {
        InetAddress inetAddress = connectedSocket.getInetAddress();
        int port = connectedSocket.getPort();
        return inetAddress.getHostAddress() + ":" + port;
    }

    /**
     * Create a connection to an existing communication endpoint. This endpoint might not yet be
     * established. This method allows to wait a few seconds to start another attempt.
     *
     * @param waitInSeconds waiting time between connection attempts (in seconds - anything below would
     *                      produce a horrible networks load.
     * @param numberAttempts how many attempts - 0 just one.
     */
    public void connect(String hostName, int waitInSeconds, int numberAttempts) {
        this.connectionAttemptThread = new ConnectionAttemptsThread(hostName, waitInSeconds, numberAttempts);
        this.checkClientAndServer();

        this.connectionAttemptThread.start();
    }

    private class ConnectionAttemptsThread extends Thread {
        private final String hostName;
        private final int waitInSeconds;
        private int numberAttempts;

        public ConnectionAttemptsThread(String hostName, int waitInSeconds, int numberAttempts) {
            this.hostName = hostName;
            this.waitInSeconds = waitInSeconds;
            this.numberAttempts = numberAttempts;
        }

        public void run() {
            StringBuilder sb = new StringBuilder();
            sb.append(" to ");
            sb.append(this.hostName);
            sb.append(" on port ");
            sb.append(StreamConnectionFactory.this.port);
            String partLogText = sb.toString();

            do {
                try {
                    Log.writeLog(this, "try to connect" + partLogText);
                    Socket socket = new Socket(this.hostName, StreamConnectionFactory.this.port);
                    Log.writeLog(this, "connect established" + partLogText);
                    StreamConnectionFactory.this.notifyConnectionCreatedListeners(
                            socket.getInputStream(), socket.getOutputStream(), false,
                            this.hostName + ":" + socket.getPort()
                    );
                    break; // success - we are done here
                } catch (IOException e) {
                    Log.writeLog(this, "connection creation failed" + partLogText);
                }
                this.numberAttempts--;
                // wait a moment
                try {
                    Thread.sleep(this.waitInSeconds * 1000);
                } catch (InterruptedException e) {
                    // ignore
                }
            } while(this.numberAttempts > 0);
            StreamConnectionFactory.this.connectionAttemptThread = null; // I am leaving now
        }
    }

    private void notifyConnectionCreatedListeners(InputStream is, OutputStream os,
              boolean asServer, String otherPeerAddress) throws IOException {


        if(this.byteFailureRate > 0) {
            // wrap it
            // is = new InputStreamFailureSimulator(this.byteFailureRate, this.gauss, is);
            Log.writeLog(this, "bit failure simulator is not implemented (yet)");
        }

        for(StreamConnectionFactoryListener listener : this.connectionCreatedListeners) {
            listener.connectionCreated(is, os, asServer, otherPeerAddress);
        }
    }

    private void checkClientAndServer() {
        if(this.serverSocket != null && this.connectionAttemptThread != null) {
            Log.writeLog(this, "This peer is open for connection requests and can also create connections." +
                    "Multiple connections to another peer could be established - be very careful what you are doing.");
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    //                          wrapper to simulate network failure                          //
    ///////////////////////////////////////////////////////////////////////////////////////////

}
