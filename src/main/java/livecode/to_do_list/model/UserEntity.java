package livecode.to_do_list.model;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String username;

    private String email;

    private String password;

    private String createAt;

    @Enumerated(EnumType.STRING)
    private Roles role;

    public enum Roles {
        USER,ADMIN,SUPER_ADMIN
    }




}
