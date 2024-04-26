package streamProtocols;

import java.io.*;

public class ourProtocolExample implements ProtocolEngine{

    @Override
    public void runProtocol(InputStream is, OutputStream os, boolean thisSideAcceptedConnection) throws IOException {

        if (thisSideAcceptedConnection) {
            for (int i = 0; i < 10; i++) {
                int wert = is.read();
                System.out.println("gelesen: " + wert);
                wert++;
                os.write(wert);
                System.out.println("gesendet: " + wert);
            }
        } else if (!thisSideAcceptedConnection){
            int tosend = 1;
            for (int i = 0; i < 10; i++) {
                os.write(tosend);
                System.out.println("gesendet: " + tosend);
                tosend = is.read();
                System.out.println("gelesen: " + tosend);
                tosend++;
            }
        }

        os.close();
        is.close();

        System.out.println("close streams");
    }
}
