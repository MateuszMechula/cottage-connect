package pl.cottageconnect.handler;

import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.cottageconnect.security.exception.EmailAlreadyExistsException;
import pl.cottageconnect.security.exception.InvalidPasswordException;
import pl.cottageconnect.security.exception.PasswordMismatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Map<Class<?>, HttpStatus> EXCEPTION_STATUS = new HashMap<>();

    static {
        EXCEPTION_STATUS.put(DataIntegrityViolationException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS.put(EntityNotFoundException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_STATUS.put(UsernameNotFoundException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_STATUS.put(MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS.put(EmailAlreadyExistsException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS.put(InvalidPasswordException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS.put(PasswordMismatchException.class, HttpStatus.BAD_REQUEST);
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
            errorMessage = handleValidationException(validationException);
        }

        ErrorDetails errorDetails = new ErrorDetails(errorId, errorMessage);

        return super.handleExceptionInternal(exception, errorDetails, headers, statusCode, request);
    }

    private String handleValidationException(MethodArgumentNotValidException validationException) {
        BindingResult bindingResult = validationException.getBindingResult();

        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()))
                    .toList();

            return errorMessages.toString();
        }

        return "Validation failed. Check the request for details.";
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(final Exception exception) {
        return doHandle(exception, getHttpStatusFromException(exception.getClass()));
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

    public HttpStatus getHttpStatusFromException(final Class<?> exception) {
        return EXCEPTION_STATUS.getOrDefault(
                exception,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
