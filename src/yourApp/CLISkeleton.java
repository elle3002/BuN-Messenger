package yourApp;

import java.io .*;

public class CLISkeleton {

    //Unterstütze Kommandos:
    private static final String ADD = "add";
    private static final String SHOW = "show";
    private static final String SENDMESSAGE = "sendMessage";
    private static final String SENDFILE = "sendFile";
    private static final String QUIT = "quit";
    private static final String HELP = "help";
    private static final String MYIP = "myIP";

    private final BufferedReader inBufferedReader;


    public static void main(String[] args) throws IOException {
            CLISkeleton userCmd = new CLISkeleton();

            CommunicationManager communicationManager = new CommunicationManager();
            Thread thread = new Thread(communicationManager);
            thread.start();

            userCmd.printUsage();
            userCmd.runCommandLoop();
        }

    public CLISkeleton() throws IOException {
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
            b.append(" [name] [message]: Sendet message an den User mit dem name");
            b.append("\n");
            b.append(SENDFILE);
            b.append(" [Name] [Filelocation]: Sendet ein File an Name (Akzeptierte Formate sind: .pdf .jpg .jpeg .png)");
            b.append("\n");
            b.append(SHOW);
            b.append(" : Zeigt dir alle eingetragenen Chatpartner");
            b.append("\n");
            b.append(MYIP);
            b.append(" : Gibt dir deine Eigene IP aus!");
            b.append("\n");
            b.append(HELP);
            b.append(" : Listet dir alle validen Kommandos auf und wie sie benutzt werden!");
            b.append("\n");
            b.append(QUIT);
            b.append(" : Beendet das Programm");
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
                            MessengerLogic.sendMessage(this.getParameter(cmdLineString, 2));
                            break;
                        case SENDFILE:
                            MessengerLogic.sendFile(this.getParameter(cmdLineString, 2));
                            break;
                        case SHOW:
                            MessengerLogic.show();
                            break;
                        case MYIP:
                            MessengerLogic.printMyIP();
                            break;
                        case HELP:
                            printUsage();
                            break;
                        case QUIT:
                            System.exit(0);
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