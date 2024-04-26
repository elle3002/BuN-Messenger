package streamProtocols;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProtocolExample implements ProtocolEngine {
    @Override
    public void runProtocol(InputStream is, OutputStream os, boolean thisSideAcceptedConnection) throws IOException {
        // run silly example
        int toSend = 1;
        if(thisSideAcceptedConnection) {
            toSend = 2;
        }
        os.write(toSend);
        System.out.println("sent: " + toSend);
        int received = is.read();
        System.out.println("received: " + received);
        System.out.println("close streams");

        os.close();
        is.close();
    }
}
