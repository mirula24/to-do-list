package livecode.to_do_list.util.response;

import livecode.to_do_list.model.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String createAt;
    private String dueDate;

    public TaskResponse(Task task) {
        this.createAt = task.getCreateAt();
        this.description = task.getDescription();
        this.dueDate = task.getDueDate();
        this.id = task.getId();
        this.status = task.getStatus().toString();
        this.title = task.getTitle();
    }


}
