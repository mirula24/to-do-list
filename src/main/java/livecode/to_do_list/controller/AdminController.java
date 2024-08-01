package livecode.to_do_list.controller;
import jakarta.validation.Valid;
import livecode.to_do_list.dto.AuthDto;
import livecode.to_do_list.dto.ChangeRole;
import livecode.to_do_list.service.AdminService;
import livecode.to_do_list.util.response.Response;
import livecode.to_do_list.util.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {
    private final AdminService adminService;

    private final String superAdminSecretKey = "superman admin turing machine gigachad sigma";

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(Pageable pageable){
        return new ResponseEntity<>(adminService.getAll(pageable),HttpStatus.OK);
    }

    @PostMapping("/users/super-admin")
    public ResponseEntity<?> register(@RequestHeader("X-Super-Admin-Secret-Key")
                                          String secret,@RequestBody AuthDto.RegisterRequest request) {

        return new ResponseEntity<>(adminService.createSuperAdmin(secret,request),HttpStatus.CREATED);
    }


    @GetMapping("/users/{id}")
    public ResponseEntity<?> getOneUser(@PathVariable Long id) {
        return Response.renderJSON(
                adminService.getOneUser(id)
        );
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @Valid @RequestBody ChangeRole req, @RequestHeader(value = "X-Admin-Secret-Key") String key) {
        if (key == null || !key.equals(superAdminSecretKey)) {
            return Response.renderJSON(
                    "Invalid Admin Secret Key",
                    HttpStatus.UNAUTHORIZED
            );
        }
        return Response.renderJSON(
                adminService.changeRole(id, req)
        );
    }
    @GetMapping("/todos")
    public ResponseEntity<?> getAllTask(@RequestHeader String Authorization,@PageableDefault Pageable pageable){
        return new ResponseEntity<>(adminService.getAllTask(Authorization,pageable),HttpStatus.OK);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<?> getOneTask(@PathVariable Long id){
        return new ResponseEntity<>(adminService.getOneTask(id),HttpStatus.OK);
    }





}
