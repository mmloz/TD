package Models;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class Response {
    @NonNull
    String code;

    @NonNull
    String body;
}
