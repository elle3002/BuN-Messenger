package yourApp;

import java.io .*;

public class CLISkeleton {

    //Unterstützte Kommandos:
    private static final String ADD = "add";
    private static final String SHOW = "show";
    private static final String SENDMESSAGE = "sendMessage";
    private static final String SENDFILE = "sendFile";
    private static final String HELP = "help";
    private static final String MYIP = "myIP";
    private static final String EXIT = "exit";

    //Server Port Numbers
    private static int serverPortNumber;
    private static final int DEFAULT_PORT_NUMBER = 3333;

    //Konsolen Input reader
    private final BufferedReader inBufferedReader;

    /**
     * Start des Chats mit dieser Main Methode
     * Setup und starten des Chats
     * @param args Startargumente
     */
    public static void main(String[] args) {
            System.out.println("Willkommen zur unserem Konsolen-Chat!");

            //ServerPortNumber ermitteln aus den Programm-Start-Argumenten (Default = DEFAULT_PORT_NUMBER)
            if (args.length > 0) {
                try {
                    // Versuch, das Argument in einen int umzuwandeln
                    serverPortNumber = Integer.parseInt(args[0]);
                // Wenn das Argument kein gültiger int ist, Standardwert verwenden
                } catch (NumberFormatException e) {
                    System.out.println("Das Argument ist keine gültige Ganzzahl. Standardwert wird verwendet.");
                    serverPortNumber = DEFAULT_PORT_NUMBER;
                }
            } else {
                // Kein Argument übergeben, also Standardwert verwenden
                serverPortNumber = DEFAULT_PORT_NUMBER;
            }

            //Server starten mit ermittelter serverPortNumber
            CommunicationManager cm = new CommunicationManager(serverPortNumber);
            Thread t = new Thread(cm);
            t.start();

            //CLISkeleton-Objekt erstellen
            CLISkeleton userCmd = new CLISkeleton();
        }

    public CLISkeleton() {
        this.inBufferedReader = new BufferedReader(new InputStreamReader(System.in));
        this.printUsage();
        this.runCommandLoop();
    }


    /**
     * Gibt die Benutzeranleitung für alle Kommandos aus
     */
    public void printUsage() {
            StringBuilder b = new StringBuilder();

            b.append("Erlaubte Kommandos:");
            b.append("\n");
            b.append(ADD);
            b.append(" [Name] [IP]: Fügt neuen Chatpartner hinzu");
            b.append("\n");
            b.append(SENDMESSAGE);
            b.append(" [Name] [Portnummer] [Nachricht]: Sendet die Nachricht an den gespeicherten Nutzer mit dem angegebenen Namen");
            b.append("\n");
            b.append(SENDFILE);
            b.append(" [Name] [Portnummer] [Filepath]: sendet ein File an den gespeicherten Nutzer mit dem angegebenen Namen");
            b.append("\n");
            b.append(SHOW);
            b.append(" zeigt dir alle eingetragenen Chatpartner an");
            b.append("\n");
            b.append(MYIP);
            b.append(" zeigt dir deine eigene IP in dem Netzwerk an");
            b.append("\n");
            b.append(HELP);
            b.append(" listet dir alle erlaubten Kommandos nochmal auf");
            b.append("\n");
            b.append(EXIT);
            b.append(" schließt dieses Programm");
            b.append("\n");

            System.out.println(b.toString());
        }

    /**
     * liest in einer Schleife Befehle von der Konsole und startet entsprechende Aktionen
     */
    public void runCommandLoop() {
            boolean again = true;

            while (again) {
                //löscht den zuletzt ausgeführten Befehl
                String cmdLineString = null;

                try {
                    //ließt commandline aus der Konsole aus
                    cmdLineString = inBufferedReader.readLine();

                    //Wenn nichts eingegeben wurde, wird die Schleife einfach wiederholt
                    if (cmdLineString == null) break;

                    //löscht Leerzeichen vorne und hinten
                    cmdLineString = cmdLineString.trim();


                //Kommando auslesen:
                    //erhälst die Stelle(index) des ersten Leerzeichens (falls keines vorhanden = -1)
                    int spaceIndex = cmdLineString.indexOf(' ');
                    //falls nicht vorhanden (spaceIndex = -1) wird spaceIndex Maximal gemacht
                    spaceIndex = spaceIndex != -1 ? spaceIndex : cmdLineString.length();

                    //Kommando wird ausgelesen (von Anfang bis spaceIndex)
                    String commandString = cmdLineString.substring(0, spaceIndex);

                    //weist je nach Kommando eine MessengerLogic Methode zu
                        //Wenn Parameter für dieses Kommando gebraucht werden, werden diese mithilfe der Methode getParameter ermittelt
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
                        case EXIT:
                            System.out.println("Auf ein baldiges Wiedersehen!");
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Unbekanntes Kommando:" + cmdLineString);
                            this.printUsage();
                            break;
                    }
                }
                //Sollte es ein Connection-Problem mit dem Konsolen-Input geben, wird das Programm beendet
                catch (IOException ex) {
                    System.err.println("IO Error: " + ex.getLocalizedMessage());
                    again = false;

                //Falls die Parameterzahl für den Befehl ungültig war, tritt diese Exception auf
                //Bahandelt wird sie mit er kurzen Nachricht und der Befehlübersicht und die Schleife wird wiederholt
                } catch (WrongParameterException e) {
                    System.out.println("Fehler: " + e.getMessage() + ". Überprüfen Sie Ihre Eingabe und versuchen Sie es erneut");
                    this.printUsage();
                }
            }
        }

    /**
     * liest aus die restlichen Parameter aus
     * @param cmdLineString gesamtstring aus der Kommandozeile
     * @param anzahlParameter anzahl der Parameter die ausgelesen werden müssen
     * @return String[] mit allen Parametern
     * @throws WrongParameterException wenn ungültige Anzahl von Paramtern angegeben wurden
     */
    private String[] getParameter(String cmdLineString, int anzahlParameter) throws WrongParameterException {
        //Array, welches zurückgegeben werden soll, wird mit der anzahl der erwarteten Parameter initialisiert
        String[] parameter = new String[anzahlParameter];

        //For Schleife die so viele Kommandos ausließt wie mit anzahlParameter angegeben wurde
        for(int i = 0; i < anzahlParameter; i++){
            //Löscht den vorherigen Teil (Kommand oder Parameter) heraus
            int spaceIndex = cmdLineString.indexOf(" ");
            cmdLineString = cmdLineString.substring(spaceIndex + 1);

            //ließt den nächten Parameter ein, wenn es nicht der letzte erwartete Parameter ist
            if (i < anzahlParameter -1) {
                //Ermittelt das nächste Leerzeichen, wenn keine gefunden wird spaceIndex -1
                spaceIndex = cmdLineString.indexOf(' ');
            }
            // wenn es der letzte Parameter ist, wird nicht mehr nach einem Leerzeichen gesucht, sondern liest den gesamten Rest ein
            else {
                spaceIndex = cmdLineString.length();
            }

            //wenn es keinen Parameter mehr gibt, wird eine Exeption geworfen
            if (spaceIndex < 0) {
                throw new WrongParameterException();
            }

            //Parameter wird in das parameter Array eingelesen
            parameter[i] = cmdLineString.substring(0, spaceIndex);
        }
        return parameter;
    }
}