package livecode.to_do_list.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDto {

    @NotBlank
    private String title;

    @NotBlank
    private String description;
    @NotBlank
    private String dueDate;
    @NotBlank
    private String status;

}
