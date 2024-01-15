package pl.cottageconnect.common.exception.exceptions;

public class ReservationAlreadyExistsException extends RuntimeException {

    public ReservationAlreadyExistsException(String message) {
        super(message);
    }
}
