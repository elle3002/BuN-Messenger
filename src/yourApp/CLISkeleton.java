package yourApp;

import streamProtocols.ourProtocolExample;
import utils.StreamConnectionFactory;
import utils.StreamConnectionFactoryListener;

import java.io .*;

public class CLISkeleton implements StreamConnectionFactoryListener {
    private static final String EXAMPLE_COMMAND_1 = "c1";
    private static final String EXAMPLE_COMMAND_2 = "c2";
    private static final String EXIT = "exit";
    private static final String CONNECT = "connect";
    private static final String OPEN = "open";
    private static final String CLOSE = "close";

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

            b.append("\n");
            b.append("\n");
            b.append("valid commands:");
            b.append("\n");
            b.append(CONNECT);
            b.append(".. connect as tcp client");
            b.append("\n");
            b.append(OPEN);
            b.append(".. open port: accept tcp connection requests");
            b.append("\n");
            b.append(CLOSE);
            b.append(".. stop accepting tcp connection requests");
            b.append("\n");
            b.append(EXAMPLE_COMMAND_1);
            b.append(".. example command 1");
            b.append("\n");
            b.append(EXAMPLE_COMMAND_2);
            b.append(".. example command 2");
            b.append("\n");
            b.append(EXIT);
            b.append(".. exit");

            System.out.println(b.toString());
        }

        public void runCommandLoop() {
            boolean again = true;

            while (again) {
                boolean rememberCommand = true;
                String cmdLineString = null;

                try {
                    // read user input
                    cmdLineString = inBufferedReader.readLine();

                    // finish that loop if less than nothing came in
                    if (cmdLineString == null) break;

                    // trim whitespaces on both sides
                    cmdLineString = cmdLineString.trim();

                    // extract command
                    int spaceIndex = cmdLineString.indexOf(' ');
                    spaceIndex = spaceIndex != -1 ? spaceIndex : cmdLineString.length();

                    // got command string
                    String commandString = cmdLineString.substring(0, spaceIndex);

                    // extract parameters string - can be empty
                    String parameterString = cmdLineString.substring(spaceIndex);
                    parameterString = parameterString.trim();

                    // start command loop
                    switch (commandString) {
                        case CONNECT:
                            this.doConnect(parameterString);
                            break;
                        case OPEN:
                            this.doOpen(parameterString);
                            break;
                        case CLOSE:
                            this.doClose();
                            break;
                        case EXAMPLE_COMMAND_1:
                            this.doExample1();
                            break;
                        case EXAMPLE_COMMAND_2:
                            this.doExample2(parameterString);
                            break;
                        case "q": // convenience
                        case EXIT:
                            again = false;
                            System.exit(1);
                            break; // end loop

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
                }
            }
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

    public void doOpen(String parameterString) throws YourAppException, IOException {
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