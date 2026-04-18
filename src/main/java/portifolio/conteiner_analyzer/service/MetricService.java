package portifolio.conteiner_analyzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portifolio.conteiner_analyzer.conteiner.Metrics;
import portifolio.conteiner_analyzer.conteiner.Node;
import portifolio.conteiner_analyzer.repository.MetricRepository;
import portifolio.conteiner_analyzer.repository.NodeRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MetricService {

    @Autowired
    private MetricRepository repository;

    @Autowired
    private NodeRepository nodeRepository;

    public Metrics createMetric(Metrics metrics) {

        Node node = nodeRepository.findById(metrics.getNode().getId())
                .orElseThrow(() -> new RuntimeException("Node not found"));
        metrics.setNode(node);
        metrics.setTimestamp(LocalDateTime.now());

        return repository.save(metrics);
    }

    public List<Metrics> getMetricsByNode(Long nodeId) {
        return repository.findByNodeId(nodeId);
    }

    public Metrics getLastMetric(long nodeId) {
        return repository.findTopByNodeIdOrderByTimestampDesc(nodeId);
    }
}
