package livecode.to_do_list.service.implement;

import livecode.to_do_list.dto.AuthDto;
import livecode.to_do_list.dto.ChangeRole;
import livecode.to_do_list.dto.TaskDto;
import livecode.to_do_list.dto.UserDto;
import livecode.to_do_list.exception.InvalidCredentialsException;
import livecode.to_do_list.exception.NotFoundException;
import livecode.to_do_list.exception.UserAlreadyExistsException;
import livecode.to_do_list.model.Roles;
import livecode.to_do_list.model.Task;
import livecode.to_do_list.model.UserEntity;
import livecode.to_do_list.repository.TaskRepository;
import livecode.to_do_list.repository.UserEntityRepository;
import livecode.to_do_list.security.JwtUtil;
import livecode.to_do_list.service.AdminService;
import livecode.to_do_list.service.AuthService;
import livecode.to_do_list.util.response.TaskAdminResponse;
import livecode.to_do_list.util.response.TaskResponse;
import livecode.to_do_list.util.response.UserResponse;
import livecode.to_do_list.util.specification.GeneralSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserEntityRepository userRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    @Override
    public UserResponse createSuperAdmin(String secret,AuthDto.RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())|| userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("User is already taken");
        }
        UserEntity user = new UserEntity();
        ZoneId jakartaZone = ZoneId.of("Asia/Jakarta");
        LocalDateTime now = LocalDateTime.now(jakartaZone);
        String createAt = now.toString();
        if (secret.contains("superman admin turing machine gigachad sigma")) {
            user.setUsername(registerRequest.getUsername());
            user.setEmail(registerRequest.getEmail());
            user.setCreateAt(createAt);
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setRole(UserEntity.Roles.SUPER_ADMIN);
        }
            userRepository.save(user);

            return new UserResponse(user);

    }

    @Override
    public UserResponse getOneUser(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponse(user);
    }

    @Override
    public Page<UserResponse> getAll(Pageable pageable) {
        Page<UserEntity> users = userRepository.findAll(pageable);
        Page<UserResponse> usersResponse = users.map(UserResponse::new);
        return usersResponse;
    }

    @Override
    public UserResponse changeRole(Long id, ChangeRole request) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(UserEntity.Roles.valueOf(request.getRole()));
        return new UserResponse(user);
    }

    @Override
    public Page<TaskAdminResponse> getAllTask(String header, Pageable pageable) {
        String extract = header.substring(7);
        String username = jwtUtil.extractUsername(extract);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(()-> new NotFoundException("user not found"));

        Page<Task> tasks = taskRepository.findAll(pageable);
        Page<TaskAdminResponse> taskResponses = tasks.map(TaskAdminResponse::new);


        return taskResponses;
    }

    @Override
    public TaskAdminResponse getOneTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(()-> new RuntimeException("Task not found"));

        return new TaskAdminResponse(task);
    }



}
