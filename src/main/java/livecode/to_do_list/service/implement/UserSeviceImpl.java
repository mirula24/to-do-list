package livecode.to_do_list.service.implement;
import livecode.to_do_list.dto.UserDto;
import livecode.to_do_list.exception.UserAlreadyExistsException;
import livecode.to_do_list.model.UserEntity;
import livecode.to_do_list.repository.UserEntityRepository;
import livecode.to_do_list.service.UserEntityService;

import livecode.to_do_list.util.specification.GeneralSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSeviceImpl implements UserEntityService {
    private final UserEntityRepository userEntityRepository;


    @Override
    public UserEntity create(UserDto request) {
       if(userEntityRepository.existsByUsername(request.getUsername())|| userEntityRepository.existsByEmail(request.getEmail()))
        {
            throw new UserAlreadyExistsException("Username is already taken");
        }
        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .username(request.getUsername())
                .build();
        return userEntityRepository.save(user);
    }

    @Override
    public Page<UserEntity> getAll(Pageable pageable, String search) {
        Specification<UserEntity> spec = GeneralSpecification.getSpecification(search);
        Page<UserEntity> users = userEntityRepository.findAll(spec,pageable);
        return users;
    }

    @Override
    public UserEntity getOne(Long id) {
        return userEntityRepository.findById(id).orElseThrow(()-> new RuntimeException(" user not found"));
    }

    @Override
    public UserEntity update(Long id, UserDto request) {
        UserEntity update = getOne(id);
        update.setPassword(request.getUsername());
        update.setEmail(request.getEmail());
        update.setPassword(request.getPassword());
        return userEntityRepository.save(update);
    }

    @Override
    public void delete(Long id) {
        UserEntity delete = getOne(id);
        userEntityRepository.delete(delete);
    }
}
