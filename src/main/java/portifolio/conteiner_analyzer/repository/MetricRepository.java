package portifolio.conteiner_analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portifolio.conteiner_analyzer.conteiner.Metric;

public interface MetricRepository extends JpaRepository<Metric, Long> {
}
