package livecode.to_do_list.service;

import livecode.to_do_list.dto.UserDto;
import livecode.to_do_list.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserEntityService {
    UserEntity create (UserDto request);
    Page<UserEntity> getAll (Pageable pageable,String search);
    UserEntity getOne(Long id);
    UserEntity update(Long id,UserDto request);
    void delete(Long id);

}
