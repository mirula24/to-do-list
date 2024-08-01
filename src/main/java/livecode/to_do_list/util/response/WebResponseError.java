package livecode.to_do_list.util.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class WebResponseError<T> {

    private String error;
    private String message;
    public WebResponseError(String error, String message) {
        this.error = error;
        this.message = message;
    }

}
