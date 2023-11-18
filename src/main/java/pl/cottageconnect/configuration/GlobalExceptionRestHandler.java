package pl.cottageconnect.configuration;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionRestHandler extends ResponseEntityExceptionHandler {

    private static final Map<Class<?>, HttpStatus> EXCEPTION_STATUS = new HashMap<>();

    static {
        EXCEPTION_STATUS.put(DataIntegrityViolationException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS.put(EntityNotFoundException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_STATUS.put(UsernameNotFoundException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_STATUS.put(MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception exception,
            @Nullable Object body,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode,
            @NonNull WebRequest request
    ) {
        final String errorId = UUID.randomUUID().toString();
        log.error("Exception: ID={}, HttpStatus={}", errorId, statusCode, exception);
        String errorMessage = "Validation failed. Check the request for details.";


        if (exception instanceof MethodArgumentNotValidException validationException) {
            BindingResult bindingResult = validationException.getBindingResult();

            if (bindingResult.hasErrors()) {
                List<String> errorMessages = bindingResult.getFieldErrors().stream()
                        .map(fieldError -> String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()))
                        .collect(Collectors.toList());

                errorMessage = String.join(", ", errorMessages);
            }
        }

        return super.handleExceptionInternal(exception, errorMessage, headers, statusCode, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(final Exception exception) {
        return doHandle(exception, getHttpStatusFromException(exception.getClass()));
    }

    public HttpStatus getHttpStatusFromException(final Class<?> exception) {
        return EXCEPTION_STATUS.getOrDefault(
                exception,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<?> doHandle(final Exception exception, final HttpStatus status) {
        final String errorId = UUID.randomUUID().toString();
        log.error("Exception: ID={}, HttpStatus={}", errorId, status, exception);

        ErrorDetails errorDetails = new ErrorDetails(errorId, exception.getMessage());

        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorDetails);
    }
}
