package livecode.to_do_list.util.response;

import livecode.to_do_list.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String createAt;

    public UserResponse(UserEntity user){
        this.id = user.getId();
        this.username = getUsername();
        this.email = user.getEmail();
        this.role = user.getRole().toString();
        this.createAt=user.getCreateAt();
    }
}
