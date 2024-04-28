package clear.solution.practisetest.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

@ResponseStatus(CONFLICT)
public class ResourceInConflictException extends RuntimeException {
    public ResourceInConflictException(String message) {
        super(message);
    }
}
