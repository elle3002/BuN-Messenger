package yourApp;

public class YourAppException extends Exception {
    public YourAppException() { super(); }
    public YourAppException(String msg) { super(msg); }
    public YourAppException(Exception e) { super(e); }
}
