package pl.cottageconnect.common.exception.exceptions;

public class MaxPhotoCountExceededException extends RuntimeException {

    public MaxPhotoCountExceededException(String message) {
        super(message);
    }
}
