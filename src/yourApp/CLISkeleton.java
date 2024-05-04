package yourApp;

import streamProtocols.ourProtocolExample;
import utils.StreamConnectionFactory;
import utils.StreamConnectionFactoryListener;

import java.io .*;

public class CLISkeleton implements StreamConnectionFactoryListener {

    //Unterstütze Kommandos:
    private static final String ADD = "add";
    private static final String SHOW = "show";
    private static final String SENDMASSAGE = "sendMessage";
    private static final String SENDFILE = "sendFile";
    private static final String OPEN = "open";
    private static final String HELP = "help";


    private static final int DEFAULT_PORT_NUMBER = 3333;

    private final BufferedReader inBufferedReader;
    private final String playerName;
    private final int portNumber;

    public static void main(String[] args) throws IOException {
            System.out.println("Welcome to [YourApp] version 0.1");

            CLISkeleton userCmd = new CLISkeleton("TestUser");

            userCmd.printUsage();
            userCmd.runCommandLoop();
        }

    public CLISkeleton(String playerName, int portNumber) throws IOException {
        this.playerName = playerName;
        this.portNumber = portNumber;
        this.inBufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public CLISkeleton(String playerName) throws IOException {
        this(playerName, DEFAULT_PORT_NUMBER);
    }

    public void printUsage() {
            StringBuilder b = new StringBuilder();

            b.append("valid commands:");
            b.append("\n");
            b.append(ADD);
            b.append(" [name] [IP]: Fügt neuen Chatpartner hinzu");
            b.append("\n");
            b.append(OPEN);
            b.append(".. open port: accept tcp connection requests");
            b.append("\n");
            b.append(SENDMASSAGE);
            b.append(" [name] [massage]: Sendet massage an den User mit dem Namen name");
            b.append("\n");
            b.append(SENDFILE);
            b.append(".. [Name] [Filelocation]: sendet ein File an Name (IP des Partners)");
            b.append("\n");
            b.append(HELP);
            b.append(".. get help, show available commands");
            b.append("\n");

            System.out.println(b.toString());
        }

        public void runCommandLoop() {
            boolean again = true;

            while (again) {
                boolean rememberCommand = true;
                String cmdLineString = null;

                try {
                    // ließt commandline aus der Console aus
                    cmdLineString = inBufferedReader.readLine();

                    // überprüft ob etwas eingegeben wurde
                    if (cmdLineString == null) break;

                    // löscht Leerzeichen vorne und hinten
                    cmdLineString = cmdLineString.trim();


                //Kommando auslesen:
                    //erhälst die Stelle(index) des ersten Leerzeichens (falls keines vorhanden = -1)
                    int spaceIndex = cmdLineString.indexOf(' ');
                    //falls nicht horhanden (spaceIndex = -1) wird spaceIndex Maximal gemacht
                    spaceIndex = spaceIndex != -1 ? spaceIndex : cmdLineString.length();

                    //Kommando wird ausgelesen (von Anfang bis spaceIndex)
                    String commandString = cmdLineString.substring(0, spaceIndex);

                    //weist je nach Kommand eine MessengerLogic Methode zu
                    switch (commandString) {
                        case ADD:
                            MessengerLogic.add(this.getParameter(cmdLineString, 2));
                            break;
                        case OPEN:
                            this.doOpen();
                            break;
                        case SENDMASSAGE:
                            MessengerLogic.sendMessage(this.getParameter(cmdLineString, 2));
                            break;
                        case SENDFILE:
                            MessengerLogic.sendFile(this.getParameter(cmdLineString, 2));
                            break;
                        case HELP:
                            printUsage();
                            break;
                        case "q": // convenience

                        default:
                            System.out.println("unknown command:" + cmdLineString);
                            this.printUsage();
                            rememberCommand = false;
                            break;
                    }
                }
                catch (IOException ex) {
                    System.err.println("io error (fatal, give up): " + ex.getLocalizedMessage());
                    again = false;
                }
                catch (YourAppException yex) {
                    System.err.println("app error (try again): " + yex.getLocalizedMessage());

                //Falls die Parameterzahl für den Befehl ungültig war, tritt diese Exeption auf
                //Bahandelt wird sie mit er kurzen Nachricht und der Befehlübersicht
                } catch (WrongParameterExeption e) {
                    System.out.println("Fehler: " + e.getMessage());
                    this.printUsage();
                }
            }
        }

    /**
     * liest aus die restlichen Parameter aus
     * @param cmdLineString gesamtstring aus der Kommandozeile
     * @param anzahlParameter anzahl der Parameter die ausgelesen werden müssen
     * @return String[] mit allen Parametern
     * @throws WrongParameterExeption wernn ungültige Anzahl von Paramtern angegeben wurden
     */
    private String[] getParameter(String cmdLineString, int anzahlParameter) throws WrongParameterExeption{
        //result Array, das zurück gegeben wird
        String[] parameter = new String[anzahlParameter];

        //For Schleife die so viele Kommandos ausließt wie angegeben
        for(int i = 0; i < anzahlParameter; i++){
            //Löscht den vorherigen Teil (Kommand oder Parameter) heraus
            int spaceIndex = cmdLineString.indexOf(" ");
            cmdLineString = cmdLineString.substring(spaceIndex + 1);

            //ließt den nächten Parameter ein
            if (i < anzahlParameter -1 ) {
                spaceIndex = cmdLineString.indexOf(' ');
            }
            // wenn es der letzte Parameter ist, wird nicht mehr beim Leerzeichen getrennt
            else {
                spaceIndex = cmdLineString.length(); // add Elias
            }
            //wenn es keine Parameter gibt wirft man Exeption
            if (spaceIndex < 0) {
                throw new WrongParameterExeption();
            }
            //Parameter werden ausgelesen
            parameter[i] = cmdLineString.substring(0, spaceIndex);
        }
        return parameter;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                           ui method implementations                                        //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void doExample1() {
        System.out.println("example 1 command called - no parameter expected");
    }

    private void doExample2(String parameterString) {
        System.out.println("example 2 command called with parameterString \"" + parameterString + "\"");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                           connection handling                                              //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private StreamConnectionFactory scFactory = null;

    private StreamConnectionFactory getStreamConnectionFactory() throws IOException {
        if(this.scFactory == null) {
            this.scFactory = new StreamConnectionFactory(this.portNumber);
            this.scFactory.addConnectionListener(this);
        }

        return this.scFactory;
    }

    public void doOpen() throws YourAppException, IOException {
        this.getStreamConnectionFactory().acceptConnectionRequests(false);
        // TODO: define a parameter that allows opening multiple connections
    }

    private void doClose() throws IOException {
        this.getStreamConnectionFactory().stopAcceptingConnectionRequests();
    }

    private void doConnect(String parameterString) throws YourAppException, IOException {
        String remoteHost = "localhost";
        int waitSecondsRetry = 5;
        int numberOfAttempts = 10;
        System.out.println("try to connect to " + remoteHost
                + " | try " + numberOfAttempts
                + " | wait " + waitSecondsRetry + " seconds");
        this.getStreamConnectionFactory().connect(remoteHost, waitSecondsRetry, numberOfAttempts);
        // TODO let hostname become a parameter of that method to and maybe connection attempts.
    }

    @Override
    public void connectionCreated(InputStream is, OutputStream os, boolean thisSideAcceptedConnection,
                                  String otherNodeAddress) throws IOException {
        System.out.println("connection created to/from " + otherNodeAddress);

        System.out.println("run example protocol " + otherNodeAddress);
        new ourProtocolExample().runProtocol(is, os, thisSideAcceptedConnection);
    }
}