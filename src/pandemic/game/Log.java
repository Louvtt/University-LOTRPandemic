package pandemic.game;

/**
 * Wrapper for custom logging
 */
public class Log {
    /** Instance of the logger */
    protected static Log sInstance; 
    /** If we output to console (or a file) */
    protected boolean logToConsole;
    // protected static final String LOG_FILE_PATH = "log.txt";

    /**
     * Level of the debugger
     */
    public enum Level {
        /** Show debug logs */
        DEBUG,
        /** Show default logs (no debug logs) */
        DEFAULT,
        /** Show only errors */
        CRITICAL
    };
    /** Level of the logger */
    protected Level level;

    /**
     * Create the logger
     */
    protected Log() {
        this.logToConsole = true;
        this.level = Level.DEFAULT;
    }

    /**
     * Returns the current instance of the logger
     * @return the current instance of the logger
     */
    static public Log Get() {
        if(Log.sInstance != null) return sInstance;
        Log.sInstance = new Log();
        return Log.sInstance;
    }

    /**
     * Sets the log to console option
     * @param logToConsole log to console
     */
    public void logToConsole(boolean logToConsole) {
        this.logToConsole = logToConsole;
    }

    /**
     * Set the level of the logger
     */
    public void setLevel(Level level) {
        this.level = level;
    }

    /**
     * Prints a debug message in system.out
     * @param msg the message to print
     */
    public void debug(String msg) {
        if(!this.logToConsole) return;

        if(this.level == Level.DEBUG) {
            System.out.print("[DEBUG] ");
            System.out.println(msg);
        }
    }

    /**
     * Prints an error message in system.err
     * @param msg the message to print
     */
    public void error(String msg) {
        System.err.print("[ERROR] ");
        System.err.println(msg);
    }

    /**
     * Prints a message in system.out
     * @param msg the message to print
     */
    public void log(String msg) {
        if(!this.logToConsole) return;

        if(this.level == Level.CRITICAL) return;
        System.out.println(msg);
    }

    /**
     * Prints a message in system.out
     * @param msg the message to print
     * @param end the end char of the message (ex: <code>\n</code> or <code>\r</code>)
     */
    public void log(String msg, char end) {
        if(!this.logToConsole) return;

        if(this.level == Level.CRITICAL) return;
        System.out.print(msg + end);
    }
}
