package yourApp;

/**
 * Eine eigene Exception, die geworfen wird, wenn ein eingegebener Name beim Senden nicht in der Liste existiert
 */
public class NoIPBehindThisNameException extends Exception {
    public NoIPBehindThisNameException() {
        super("Keine IP unter diesem Namen gefunden!");
    }
}
