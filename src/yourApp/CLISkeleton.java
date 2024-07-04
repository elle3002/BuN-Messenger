package yourApp;

import java.io .*;

public class CLISkeleton {

    //Unterstütze Kommandos:
    private static final String ADD = "add";
    private static final String SHOW = "show";
    private static final String SENDMESSAGE = "sendMessage";
    private static final String SENDFILE = "sendFile";
    private static final String HELP = "help";
    private static final String MYIP = "myIP";

    private static final int DEFAULT_PORT_NUMBER = 3333;
    private static int ownPortNumber;

    private final BufferedReader inBufferedReader;
    private final String playerName;
    private final int portNumber;

    public static void main(String[] args) throws IOException {
            System.out.println("Welcome to Chat With Friends");

            if (args.length > 0) {
                try {
                    // Versuch, das Argument in einen int umzuwandeln
                    ownPortNumber = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    // Wenn das Argument kein gültiger int ist, Standardwert verwenden
                    System.out.println("Das Argument ist keine gültige Ganzzahl. Standardwert wird verwendet.");
                    ownPortNumber = DEFAULT_PORT_NUMBER;
                }
            } else {
                // Kein Argument übergeben, also Standardwert verwenden
                ownPortNumber = DEFAULT_PORT_NUMBER;
            }

            CommunicationManager cm = new CommunicationManager(ownPortNumber);
            Thread t = new Thread(cm);
            t.start();

            CLISkeleton userCmd = new CLISkeleton("TestUser", ownPortNumber);

            userCmd.printUsage();
            userCmd.runCommandLoop();
        }

    public CLISkeleton(String playerName, int portNumber) throws IOException {
        this.playerName = playerName;
        this.portNumber = portNumber;
        this.inBufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }


    public void printUsage() {
            StringBuilder b = new StringBuilder();

            b.append("valid commands:");
            b.append("\n");
            b.append(ADD);
            b.append(" [name] [IP]: Fügt neuen Chatpartner hinzu");
            b.append("\n");
            b.append(SENDMESSAGE);
            b.append(" [name] [port] [message]: Sendet message an den User mit dem Namen name");
            b.append("\n");
            b.append(SENDFILE);
            b.append(".. [Name] [port] [Filelocation]: sendet ein File an Name (IP des Partners)");
            b.append("\n");
            b.append(SHOW);
            b.append(" zeigt dir alle eingetragenen Chatpartner)");
            b.append("\n");
            b.append(MYIP);
            b.append(" zeigt dir deine IP an)");
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

                    // überprüft, ob etwas eingegeben wurde
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
                        case SENDMESSAGE:
                            MessengerLogic.sendMessage(this.getParameter(cmdLineString, 3));
                            break;
                        case SENDFILE:
                            MessengerLogic.sendFile(this.getParameter(cmdLineString, 3));
                            break;
                        case SHOW:
                            MessengerLogic.show();
                            break;
                        case MYIP:
                            System.out.println("Meine lokale IP-Adresse ist: " +  MessengerLogic.printMyIP());
                            break;
                        case HELP:
                            printUsage();
                            break;
                        default:
                            System.out.println("unknown command:" + cmdLineString);
                            this.printUsage();
                            break;
                    }
                }
                catch (IOException ex) {
                    System.err.println("io error (fatal, give up): " + ex.getLocalizedMessage());
                    again = false;

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
            if (i < anzahlParameter -1) {
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
}