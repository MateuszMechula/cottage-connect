package pl.cottageconnect.security.exception;

public class UserIdMismatchException extends RuntimeException {

    public UserIdMismatchException(String message) {
        super(message);
    }
}
