package streamProtocols;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ProtocolEngine {
    void runProtocol(InputStream is, OutputStream os, boolean thisSideInitiatedConnection) throws IOException;
}
