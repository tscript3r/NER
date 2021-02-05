package pl.tscript3r.ner.exception;

public class NERException extends RuntimeException {

    public NERException() {
    }

    public NERException(String message) {
        super(message);
    }

    public NERException(String message, Throwable cause) {
        super(message, cause);
    }

}
