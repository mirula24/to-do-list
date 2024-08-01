package livecode.to_do_list.service;

import livecode.to_do_list.dto.AuthDto;
import livecode.to_do_list.dto.ChangeRole;
import livecode.to_do_list.dto.TaskDto;
import livecode.to_do_list.dto.UserDto;
import livecode.to_do_list.util.response.TaskAdminResponse;
import livecode.to_do_list.util.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    UserResponse createSuperAdmin(String secret,AuthDto.RegisterRequest registerRequest);
    UserResponse getOneUser(Long id);
    Page<UserResponse> getAll(Pageable pageable);
    UserResponse changeRole(Long id, ChangeRole request);
    Page<TaskAdminResponse> getAllTask(String header,Pageable pageable);
    TaskAdminResponse getOneTask(Long id);
}
