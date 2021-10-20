package exception;


public class ReplacementException extends Exception {
    public ReplacementException(String message) {
        super(message);
    }

    public ReplacementException(String message, Throwable cause) {
        super(message, cause);
    }
}
