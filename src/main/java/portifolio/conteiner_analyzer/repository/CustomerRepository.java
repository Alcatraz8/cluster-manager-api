package portifolio.conteiner_analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portifolio.conteiner_analyzer.entities.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findById(Long id);
}
