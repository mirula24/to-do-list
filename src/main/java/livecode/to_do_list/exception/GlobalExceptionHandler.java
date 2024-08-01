package livecode.to_do_list.exception;
import livecode.to_do_list.util.response.WebResponseError;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<WebResponseError> handleBadRequestException(BadRequestException ex) {
        WebResponseError error = new WebResponseError("Bad Request", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<WebResponseError> handleUnauthorizedException(UnauthorizedException ex) {
        WebResponseError error = new WebResponseError("Unauthorized", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<WebResponseError> handleForbiddenException(ForbiddenException ex) {
        WebResponseError error = new WebResponseError("Forbidden", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<WebResponseError> handleNotFoundException(NotFoundException ex) {
        WebResponseError error = new WebResponseError("Not Found", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WebResponseError> handleGenericException(Exception ex) {
        WebResponseError error = new WebResponseError("Internal Server Error", "An unexpected error occurred");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
