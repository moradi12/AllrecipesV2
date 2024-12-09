package Allrecipes.Recipesdemo.Advice;

import Allrecipes.Recipesdemo.Exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({
            UserNotFoundException.class,
            RecipeNotFoundException.class
    })
    public ResponseEntity<ErrDetails> handleNotFoundExceptions(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse("NOT_FOUND", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            InvalidRecipeDataException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ErrDetails> handleBadRequestExceptions(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse("BAD_REQUEST", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            UsernameAlreadyTakenException.class,
            EmailAlreadyTakenException.class
    })
    public ResponseEntity<ErrDetails> handleConflictExceptions(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse("CONFLICT", ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<ErrDetails> handleForbiddenException(UnauthorizedActionException ex, HttpServletRequest request) {
        return buildErrorResponse("FORBIDDEN", ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrDetails>> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ErrDetails> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ErrDetails(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        logger.error("Validation errors: {}", fieldErrors);
        return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrDetails> handleGenericException(Exception ex, HttpServletRequest request) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        return buildErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrDetails> buildErrorResponse(String key, String value, HttpStatus status) {
        ErrDetails error = new ErrDetails(key, value);
        logger.error("{}: {}", status.value(), value);
        return new ResponseEntity<>(error, status);
    }
}