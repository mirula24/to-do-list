package livecode.to_do_list.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotBlank
    private String username;

    ZoneId jakartaZone = ZoneId.of("Asia/Jakarta");
    LocalDateTime now = LocalDateTime.now(jakartaZone);
    private String createAt = now.toString();

    @Email(message = "Email should be valid")
    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
