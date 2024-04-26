package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface StreamConnectionFactoryListener {
    /**
     * Notification: a new stream connection was established
     * @param is
     * @param os
     * @param asServer: true - connection request arrived this local peer /
     *                false: this local peer made a successful connection attempt
     * @param otherPeerAddress (L2 or L3) endpoint address of connected peer
     */
    void connectionCreated(InputStream is, OutputStream os, boolean asServer, String otherPeerAddress) throws IOException;
}
