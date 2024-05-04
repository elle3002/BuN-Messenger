package yourApp;

public class WrongParameterExeption extends Exception {
    public WrongParameterExeption() {
        super("Falsche Anzahl an Parametern");
    }
}
