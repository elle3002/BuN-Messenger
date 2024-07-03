import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class IPtest {

    private static String myIP = "unknown";

    static {
        try {
            myIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println("Deine IP-Adresse kann nicht ermittelt werden!");
        }
    }

    public static void main(String[] args) throws IOException {
        printMyIP();
    }

    public static void printMyIP() { // getLocalIpAdress
        System.out.println("Meine aktuelle IP-Adresse ist: " + myIP);
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); // ruft alle Netzwerkschnitstellen auf
            while(interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement(); // durchläuft alle Schnitstellen und speichert diese
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses(); // gibt alle IP-Adr. von den Schnitst. in eine Enum. aller Objekte
                while(addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement(); // durchläuft die jede IP-Adr.
                    // Filtert loopback und non-site Adressen raus
                    if (!inetAddress.isLoopbackAddress() && inetAddress.isSiteLocalAddress()) {
                        // für Linux-OS
                        if (networkInterface.getName().equals("wlp2s0")) {
                            System.out.println("Meine lokale IP-Adresse ist, schicke diese an deinem Chatpartner: " + inetAddress.getHostAddress());
                            return;
                        }
                    } else if (!inetAddress.isLoopbackAddress() && inetAddress.isSiteLocalAddress()){ // für Windows-OS
                        System.out.println("Meine locale IP-Adresse ist, schicke diese an deinem Chatpartner:: " + inetAddress.getHostAddress());
                        return; // kehre zurück nachdem IP-Adresse gefunden wurde
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Die öffentliche IP-Adresse kann nicht ermittelt werden: " + e.getMessage());
        }
    }
}


