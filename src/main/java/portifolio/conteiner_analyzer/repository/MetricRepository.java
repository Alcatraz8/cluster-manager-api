package portifolio.conteiner_analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portifolio.conteiner_analyzer.conteiner.Metrics;

import java.util.List;

public interface MetricRepository extends JpaRepository<Metrics, Long> {
    List<Metrics> findByNodeId(Long nodeId);

    Metrics findTopByNodeIdOrderByTimestampDesc(Long nodeId);
}
