package exceptions;

public class BookQueryException extends Exception {
    public BookQueryException(String message) {
        super(message);
    }

    public BookQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
