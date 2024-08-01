package livecode.to_do_list.service;

import livecode.to_do_list.dto.AuthDto;
import livecode.to_do_list.model.UserEntity;
import livecode.to_do_list.util.response.AuthResponse;
import livecode.to_do_list.util.response.RegisterResponse;

public interface AuthService {
    AuthResponse login(AuthDto.LoginRequest loginRequest);
    RegisterResponse register(AuthDto.RegisterRequest registerRequest);
    String refreshToken(String refreshToken);
    UserEntity getUserAuthenticated();
}
