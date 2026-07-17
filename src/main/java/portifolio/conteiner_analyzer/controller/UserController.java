package portifolio.conteiner_analyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import portifolio.conteiner_analyzer.DTO.request.UserRequestDTO;
import portifolio.conteiner_analyzer.DTO.response.UserResponseDTO;
import org.springframework.web.bind.annotation.*;
import portifolio.conteiner_analyzer.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return ResponseEntity
                .ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity
                .ok(service.findById(id));
    }

    @PostMapping("/signUp")
    public ResponseEntity<UserResponseDTO> signup(@RequestBody UserRequestDTO dto) {
         UserResponseDTO createdUser = service.signup(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @PostMapping("/signIn")
    public ResponseEntity<UserResponseDTO> signIn(@RequestBody UserRequestDTO loginRequest) {
        service.signIn(loginRequest);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
                                                      @RequestBody UserRequestDTO dto){
        UserResponseDTO updatedUser = service.updateUser(id, dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        service.deleteUser(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}