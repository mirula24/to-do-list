package livecode.to_do_list.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import livecode.to_do_list.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private Status status ;

    @NotBlank
    private String dueDate ;

}
