package net.wiicart.karatasi.exception;

/**
 * Exception that signifies some type of failure has occurred while reaching a document.
 */
@SuppressWarnings("unused")
public final class LoadFailureException extends Exception {

    /**
     * Custom Codes
     * 700 - unknown host
     * 750 - file not found
     */
    private final int code;

    public LoadFailureException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public LoadFailureException(int code, String message) {
        super(message);
        this.code = code;
    }

    public LoadFailureException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public LoadFailureException(int code) {
        super();
        this.code = code;
    }

    /**
     * The HTTP status code, or 0 if N/A
     * @return An int.
     */
    public int code() {
        return code;
    }
}
