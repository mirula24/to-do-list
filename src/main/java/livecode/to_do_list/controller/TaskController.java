package livecode.to_do_list.controller;
import jakarta.validation.Valid;
import livecode.to_do_list.dto.TaskDto;
import livecode.to_do_list.dto.UpdateDto;
import livecode.to_do_list.dto.UpdateStatusDto;
import livecode.to_do_list.model.Task;
import livecode.to_do_list.model.UserEntity;
import livecode.to_do_list.service.TaskService;
import livecode.to_do_list.util.response.PageResponseWrapper;
import livecode.to_do_list.util.response.Response;
import livecode.to_do_list.util.response.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@Validated
public class TaskController {
    private final TaskService taskService;


    @PostMapping
    public ResponseEntity<?> create(
            @RequestHeader String Authorization, @Valid @RequestBody TaskDto request){
        return new ResponseEntity(taskService.create(Authorization,request),HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestHeader String Authorization,
                                    @PageableDefault(page = 0, size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable,
                                    @ModelAttribute TaskDto taskDto
    ) {
        Page<TaskResponse> result = taskService.getAll(Authorization, pageable, taskDto);
        PageResponseWrapper<TaskResponse> responseWrapper = new PageResponseWrapper<>(result);
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@RequestHeader String Authorization,@PathVariable Long id){
        return new ResponseEntity(taskService.getOne(Authorization,id),HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestHeader String Authorization,@PathVariable Long id, @RequestBody UpdateDto update) {
        return ResponseEntity.ok(taskService.update(Authorization,id,update));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateStatus(@RequestHeader String Authorization,
                                          @PathVariable Long id,
                                          @RequestBody UpdateStatusDto status) {
        return ResponseEntity.ok(taskService.updateStatus(Authorization,id,status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@RequestHeader String Authorization,@PathVariable Long id) {
        taskService.delete(Authorization,id);
        return ResponseEntity.noContent().build();
    }



}
