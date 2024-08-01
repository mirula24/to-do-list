package livecode.to_do_list.service.implement;

import livecode.to_do_list.dto.TaskDto;
import livecode.to_do_list.dto.UpdateDto;
import livecode.to_do_list.dto.UpdateStatusDto;
import livecode.to_do_list.exception.NotFoundException;
import livecode.to_do_list.model.Status;
import livecode.to_do_list.model.Task;
import livecode.to_do_list.model.UserEntity;
import livecode.to_do_list.repository.TaskRepository;
import livecode.to_do_list.repository.UserEntityRepository;
import livecode.to_do_list.security.JwtUtil;
import livecode.to_do_list.service.TaskService;
import livecode.to_do_list.util.response.TaskResponse;
import livecode.to_do_list.util.specification.GeneralSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static livecode.to_do_list.model.Task.Status.PENDING;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{
    private final TaskRepository taskRepository;
    private final UserEntityRepository userEntityRepository;
    private final JwtUtil jwtUtil;

    @Override
    public TaskResponse create(String token , TaskDto request) {
        String extract = token.substring(7);
        String username = jwtUtil.extractUsername(extract);
        UserEntity user = userEntityRepository.findByUsername(username).orElseThrow((()-> new RuntimeException("User not found")));
        ZonedDateTime create_at = ZonedDateTime.now(ZoneId.of("Asia/Jakarta"));
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String createTask = create_at.format(formatter);


        Task task  = Task.builder()
                .userEntity(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .createAt(createTask)
                .status(PENDING)
                .dueDate(request.getDueDate())
                .build();
        taskRepository.save(task);

        return new TaskResponse(task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getStatus().toString(),
                task.getCreateAt()

        );
    }

    @Override
    public Page<TaskResponse> getAll(String token,Pageable pageable, TaskDto seacrh) {
        String extract = token.substring(7);
        String username = jwtUtil.extractUsername(extract);
        UserEntity user = userEntityRepository.findByUsername(username).orElseThrow(()-> new NotFoundException("user not found"));
        Task searching  =  Task.builder()
                .description(seacrh.getDescription())
                .userEntity(user)
                .title(seacrh.getTitle())
                .build();
        Specification<Task> specification = GeneralSpecification.getSpecification(searching);
        Page<Task> tasks = taskRepository.findAll(specification, pageable);

        Page<TaskResponse> taskResponses = tasks.map(TaskResponse::new);

        return taskResponses;
    }

    @Override
    public TaskResponse getOne(String token,Long id) {
        String Authorization = token.substring(7);
            String username = jwtUtil.extractUsername(Authorization);
            UserEntity user = userEntityRepository.findByUsername(username).orElseThrow(()-> new NotFoundException("User Not found"));
            Task task = taskRepository.findById(id).orElseThrow(()-> new NotFoundException("Taks not found"));
            TaskResponse response = new TaskResponse(task);
            if (user == task.getUserEntity()){
                return response;
            }
           return null;

    }

    @Override
    public TaskResponse update(String token,Long id, UpdateDto request) {
        String Authorization = token.substring(7);
        String username = jwtUtil.extractUsername(Authorization);
        UserEntity user = userEntityRepository.findByUsername(username).orElseThrow(()-> new NotFoundException("User Not found"));
        Task task = taskRepository.findById(id).orElseThrow(()-> new NotFoundException("Taks not found"));
        if (user == task.getUserEntity()) {
            task.setDueDate(request.getDueDate());
            task.setTitle(request.getTitle());
            task.setDescription(request.getDescription());
            task.setStatus(Task.Status.valueOf(request.getStatus()));
            taskRepository.save(task);
        }
        else{
            throw new RuntimeException("User not have task with id :"+id);
        }
        return new TaskResponse(task);

    }
    @Override
    public TaskResponse updateStatus(String token, Long id, UpdateStatusDto status) {
        String Authorization = token.substring(7);
        String username = jwtUtil.extractUsername(Authorization);
        UserEntity user = userEntityRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User Not found"));
        Task updateStatus = taskRepository.findById(id).orElseThrow(()-> new RuntimeException("Taks not found"));
        if (user.equals(updateStatus.getUserEntity())) {
            Task.Status taskStatus = Task.Status.valueOf(status.getStatus());
            updateStatus.setStatus(taskStatus);
        }
        taskRepository.save(updateStatus);
        return new TaskResponse(updateStatus);
    }

    @Override
    public void delete(String token,Long id) {
        String Authorization = token.substring(7);
        String username = jwtUtil.extractUsername(Authorization);
        UserEntity user = userEntityRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not found"));
        Task deletetask = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Taks not found"));
        if (user.equals(deletetask.getUserEntity())) {
        taskRepository.delete(deletetask);
        }
    }

}