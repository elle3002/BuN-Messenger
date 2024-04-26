package utils;

public class Log {
    public Log() {
    }

    public static StringBuilder startLog(Object o, String parameter) {
        return startLog(o.getClass(), parameter);
    }

    public static StringBuilder startLog(Object o) {
        return startLog((Object)o, (String)null);
    }

    public static StringBuilder startLog(Class c) {
        return startLog((Class)c, (String)null);
    }

    public static StringBuilder startLog(Class c, String parameter) {
        StringBuilder sb = new StringBuilder();
        sb.append(c.getSimpleName());
        if (parameter != null && parameter.length() > 0) {
            sb.append("(");
            sb.append(parameter);
            sb.append(")");
        }

        sb.append(": ");
        return sb;
    }

    public static void writeLog(Object o, String parameter, String message) {
        System.out.println(startLog(o, parameter) + message);
    }

    public static void writeLog(Object o, String message) {
        writeLog((Object)o, (String)null, message);
    }

    public static void writeLog(Class c, String parameter, String message) {
        System.out.println(startLog(c, parameter) + message);
    }

    public static void writeLog(Class c, String message) {
        writeLog((Class)c, (String)null, message);
    }

    public static void writeLogErr(Object o, String parameter, String message) {
        System.err.println(startLog(o, parameter) + message);
    }

    public static void writeLogErr(Object o, String message) {
        writeLogErr((Object)o, (String)null, message);
    }

    public static void writeLogErr(Class c, String parameter, String message) {
        System.err.println(startLog(c, parameter) + message);
    }

    public static void writeLogErr(Class c, String message) {
        writeLogErr((Class)c, (String)null, message);
    }
}
