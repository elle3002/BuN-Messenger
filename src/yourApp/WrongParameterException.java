package yourApp;

/**
 * Eine eigene Exception, die geworfen wird, wenn in der Konsole eine falsche Anzahl an Parametern angegeben wurde
 */
public class WrongParameterException extends Exception {
    public WrongParameterException() {
        super("Falsche Anzahl an Parametern");
    }
}
