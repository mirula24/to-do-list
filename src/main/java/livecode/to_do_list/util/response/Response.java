package livecode.to_do_list.util.response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class Response {
    public static <T> ResponseEntity<?> renderJSON(T data, HttpStatus httpStatus) {
        WebResponse<T> response = WebResponse.<T>builder()
                .data(data)
                .build();
        return ResponseEntity.status(httpStatus).body(response);
    }

    public static <T> ResponseEntity<?> renderJSON(T data, String message) {
        return renderJSON(data, HttpStatus.OK);
    }

    public static <T> ResponseEntity<?> renderJSON(T data) {
        return renderJSON(data, "Success");
    }

}

