package yourApp;

public class NoIPBehindThisNameExeption extends Exception {
    public NoIPBehindThisNameExeption() {
        super("Keine IP unter diesem Namen gefunden!");
    }
}
