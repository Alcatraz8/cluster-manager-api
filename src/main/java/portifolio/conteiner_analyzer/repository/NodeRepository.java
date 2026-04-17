package portifolio.conteiner_analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portifolio.conteiner_analyzer.conteiner.Node;

public interface NodeRepository extends JpaRepository<Node, Long> {
}
