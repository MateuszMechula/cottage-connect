package pl.cottageconnect.handler;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class ErrorDetails {
    private final Date timestamp;
    private final String errorId;
    private final String message;

    public ErrorDetails(String errorId, String message) {
        this.timestamp = new Date();
        this.errorId = errorId;
        this.message = message;
    }

}
