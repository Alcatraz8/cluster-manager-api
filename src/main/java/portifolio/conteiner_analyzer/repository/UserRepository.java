package portifolio.conteiner_analyzer.repository;

import  portifolio.conteiner_analyzer.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLogin(String login);
    Optional<User> findByLogin(String login);
    Optional<User> findById(Long id);
}

