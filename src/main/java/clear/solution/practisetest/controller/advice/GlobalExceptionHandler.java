package clear.solution.practisetest.controller.advice;

import clear.solution.practisetest.exception.ResourceInConflictException;
import clear.solution.practisetest.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.getLocalizedMessage());
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(ResourceInConflictException.class)
    ProblemDetail handleConflict(ResourceInConflictException ex) {
        return ProblemDetail.forStatusAndDetail(CONFLICT, ex.getLocalizedMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail handleBadRequest(IllegalArgumentException ex) {
        return ProblemDetail.forStatusAndDetail(BAD_REQUEST, ex.getLocalizedMessage());
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    ProblemDetail handle(Exception ex) {
        log.error("Unexpected error: ", ex);
        var problem = ProblemDetail.forStatusAndDetail(INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
        problem.setTitle("Unexpected error");
        return problem;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        var errors = new HashMap<String, Set<String>>();
        ex.getAllErrors().forEach(e -> {
            var name = e.getObjectName();
            if (e instanceof FieldError fe) {
                name = fe.getField();
            }

            errors.computeIfAbsent(name, key -> new HashSet<>())
                    .add(e.getDefaultMessage());
        });

        var problemDetail = ex.getBody();
        problemDetail.setTitle("Validation error");
        problemDetail.setProperty("errors", errors);
        return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }
}
