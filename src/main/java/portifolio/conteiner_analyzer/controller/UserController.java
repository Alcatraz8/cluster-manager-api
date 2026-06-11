package portifolio.conteiner_analyzer.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import portifolio.conteiner_analyzer.configuration.Views;
import portifolio.conteiner_analyzer.entities.User;
import org.springframework.web.bind.annotation.*;
import portifolio.conteiner_analyzer.repository.UserRepository;
import portifolio.conteiner_analyzer.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/signUp")
    public ResponseEntity<String> signup(@RequestBody User newUser) {
         service.signup(newUser);
        return ResponseEntity.ok("User created successfully");
    }

    @PostMapping("/signIn")
    public ResponseEntity<String> signIn (@RequestBody User loginRequest) {
        service.signIn(loginRequest.getLogin(), loginRequest.getPassword());
        return ResponseEntity.ok("Welcome!");
    }

    @JsonView(Views.UserView.class)
    @GetMapping
    public List<User> findAll() {
        return repository.findAll();
    }

    @JsonView(Views.UserView.class)
    @GetMapping("/{id}")
    public Optional<User> findById(@PathVariable Long id) {
        return repository.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser){

        repository.findById(id)
                .map( user -> {
                    user.setLogin(updatedUser.getLogin());
                    user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                    
                    repository.save(user);

                    return ResponseEntity.ok(user);
                });
        return  ResponseEntity.ok("User updated successfully");

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.ok("deleted");
    }
}