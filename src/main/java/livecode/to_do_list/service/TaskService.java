package livecode.to_do_list.service;

import livecode.to_do_list.dto.TaskDto;
import livecode.to_do_list.dto.UpdateDto;
import livecode.to_do_list.dto.UpdateStatusDto;
import livecode.to_do_list.model.Task;
import livecode.to_do_list.util.response.TaskResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TaskService {
    TaskResponse create(String token, TaskDto request);
    Page<TaskResponse> getAll(String token, Pageable pageable, TaskDto seacrh);
    TaskResponse getOne(String token ,Long id);
    TaskResponse update(String token,Long id, UpdateDto request);
    TaskResponse updateStatus(String token,Long id, UpdateStatusDto status);
    void delete(String token,Long id);

}
