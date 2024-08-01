package livecode.to_do_list.controller;
import jakarta.validation.Valid;
import livecode.to_do_list.dto.AuthDto;
import livecode.to_do_list.dto.UserDto;
import livecode.to_do_list.service.AuthService;
import livecode.to_do_list.service.UserEntityService;
import livecode.to_do_list.util.response.AuthResponse;
import livecode.to_do_list.util.response.Response;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated

public class AuthContoller {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthDto.LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody AuthDto.RegisterRequest registerRequest
    ) {
        return new ResponseEntity(authService.register(registerRequest),HttpStatus.CREATED);
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }
}
