package portifolio.conteiner_analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portifolio.conteiner_analyzer.entities.conteiner.Node;

import java.util.Optional;

public interface NodeRepository extends JpaRepository<Node, Long> {
    Optional<Node> findByName(String nodeName);
}
