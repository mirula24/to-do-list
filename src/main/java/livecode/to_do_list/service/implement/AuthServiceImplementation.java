package livecode.to_do_list.service.implement;


import livecode.to_do_list.dto.AuthDto;
import livecode.to_do_list.exception.InvalidCredentialsException;
import livecode.to_do_list.exception.InvalidTokenException;
import livecode.to_do_list.exception.UserAlreadyExistsException;
import livecode.to_do_list.model.UserEntity;
import livecode.to_do_list.repository.UserEntityRepository;
import livecode.to_do_list.security.JwtUtil;
import livecode.to_do_list.service.AuthService;
import livecode.to_do_list.util.response.AuthResponse;
import livecode.to_do_list.util.response.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthServiceImplementation implements AuthService {

    private static final int MIN_PASSWORD_LENGTH = 8;

    private final AuthenticationManager authenticationManager;
    private final UserEntityRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthServiceImplementation(AuthenticationManager authenticationManager,
                                     UserEntityRepository userRepository,
                                     PasswordEncoder passwordEncoder,
                                     JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponse login(AuthDto.LoginRequest loginRequest) {
        try {
            UserEntity userEntity = userRepository.findByEmail(loginRequest.getEmail());
            if (userEntity == null) {
                throw new InvalidCredentialsException("Invalid email or password");
            }

            if (passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())) {
                UserDetails userDetails = org.springframework.security.core.userdetails.User
                        .withUsername(userEntity.getUsername())
                        .password(userEntity.getPassword())
                        .authorities(userEntity.getRole().name())
                        .build();

                String accessToken = jwtUtil.generateAccessToken(userDetails);
                String refreshToken = jwtUtil.generateRefreshToken(userDetails);
                return new AuthResponse(accessToken, refreshToken);
            } else {
                throw new InvalidCredentialsException("Invalid email or password");
            }
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }

    @Override
    public RegisterResponse register(AuthDto.RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())|| userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("Username is already taken");
        }

        List<String> passwordErrors = validatePassword(registerRequest.getPassword());
        if (!passwordErrors.isEmpty()) {
            throw new IllegalArgumentException("Password does not meet strength requirements: " + String.join(", ", passwordErrors));
        }

        UserEntity user = new UserEntity();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setRole(UserEntity.Roles.USER);
        userRepository.save(user);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(UserEntity.Roles.USER.name())
                .build();
        return new RegisterResponse(user.getId(),user.getUsername(), user.getEmail());
    }

    @Override
    public String refreshToken(String refreshToken) {
        if (jwtUtil.validateRefreshToken(refreshToken)) {
            String username = jwtUtil.extractUsername(refreshToken);
            UserDetails userDetails = userRepository.findByUsername(username)
                    .map(user -> org.springframework.security.core.userdetails.User
                            .withUsername(user.getUsername())
                            .password(user.getPassword())
                            .authorities(user.getRole().name())
                            .build())
                    .orElseThrow(() -> new InvalidTokenException("User not found for the given token"));

            String newAccessToken = jwtUtil.generateAccessToken(userDetails);
            return newAccessToken;
        }
        throw new InvalidTokenException("Invalid refresh token");
    }

    @Override
    public UserEntity getUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new RuntimeException("Unauthorize, user not found")
        );
        return user;
    }

    private List<String> validatePassword(String password) {
        List<String> errors = new ArrayList<>();

        if (password.length() < MIN_PASSWORD_LENGTH) {
            errors.add("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long");
        }
        if (!password.matches(".*[A-Z].*")) {
            errors.add("Password must contain at least one uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            errors.add("Password must contain at least one lowercase letter");
        }
        if (!password.matches(".*\\d.*")) {
            errors.add("Password must contain at least one digit");
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            errors.add("Password must contain at least one special character");
        }

        return errors;
    }


}