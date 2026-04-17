package portifolio.conteiner_analyzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portifolio.conteiner_analyzer.conteiner.Metric;
import portifolio.conteiner_analyzer.repository.MetricRepository;

import java.time.LocalDateTime;

@Service
public class MetricService {

    @Autowired
    private MetricRepository repository;

    public Metric saveMetric(Metric metric) {

        validate(metric);

        metric.setTimestamp(LocalDateTime.now());
        return repository.save(metric);
    }

    private void validate(Metric metric) {
        if (metric.getCpuUsage() < 0 || metric.getCpuUsage() > 100) {
            throw new RuntimeException("invalid CPU");
        }
    }
}
