package pl.cottageconnect.security.exception;

public class MissingUserException extends RuntimeException {

    public MissingUserException(String message) {
        super(message);
    }
}
