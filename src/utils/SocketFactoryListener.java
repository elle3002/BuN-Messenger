package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SocketFactoryListener {
    void connectionCreated(InputStream is, OutputStream os, String remoteAddress) throws IOException;
}
