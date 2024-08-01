package livecode.to_do_list.security;
import livecode.to_do_list.model.UserEntity;
import livecode.to_do_list.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSecurity {
    private final UserEntityRepository userRepository;

    public boolean hasUserId(Authentication authentication, Long userId) {
        UserEntity user = userRepository.findByUsername(authentication.getName()).orElse(null);
        Long currentUserId = user.getId();
        return currentUserId.equals(userId);
    }

    public boolean isUser(Authentication authentication, int userId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        System.out.println(principal instanceof UserEntity);

        if (!(principal instanceof User)) {
            return false;
        }
        UserEntity user = (UserEntity) principal;
        return user.getId() == userId;
    }
}
