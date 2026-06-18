package portifolio.conteiner_analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portifolio.conteiner_analyzer.entities.conteiner.Metric;

import java.util.List;

public interface MetricRepository extends JpaRepository<Metric, Long> {
    List<Metric> findByNodeId(Long nodeId);

    Metric findTopByNodeIdOrderByTimestampDesc(Long nodeId);
}
