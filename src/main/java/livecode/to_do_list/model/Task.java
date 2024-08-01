package livecode.to_do_list.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity userEntity;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;



    public enum Status {
        PENDING,
        COMPLETED,
        IN_PROGRESS
    }

    private String createAt;
    private String dueDate;

}
