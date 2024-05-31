package yourApp;

public class NoIPBehindThisNameExeption extends Throwable {
    public NoIPBehindThisNameExeption() {
        super("Keine IP unter diesem Namen gefunden!");
    }
}
