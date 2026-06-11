package portifolio.conteiner_analyzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import portifolio.conteiner_analyzer.entities.User;
import portifolio.conteiner_analyzer.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User signup(User user) {
        if (repository.existsByLogin(user.getLogin())) {
            throw new RuntimeException("User name already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return repository.save(user);
    }

    public User signIn(String login, String password) {
        User user = repository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Ivalid email or password");
        }

        return user;
    }

}


