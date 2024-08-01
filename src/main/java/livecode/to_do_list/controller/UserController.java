package livecode.to_do_list.controller;
import jakarta.validation.Valid;
import livecode.to_do_list.dto.UserDto;
import livecode.to_do_list.service.UserEntityService;
import livecode.to_do_list.util.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserEntityService userEntityService;






}
