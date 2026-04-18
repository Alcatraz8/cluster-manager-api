package portifolio.conteiner_analyzer.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portifolio.conteiner_analyzer.configuration.Views;
import portifolio.conteiner_analyzer.conteiner.Metrics;
import portifolio.conteiner_analyzer.repository.MetricRepository;
import portifolio.conteiner_analyzer.service.MetricService;

import java.util.List;

@RestController
@RequestMapping("/metrics")
public class MetricController {

    @Autowired
    private MetricService service;

    @Autowired
    private MetricRepository repository;

    @JsonView({Views.MetricView.class})
    @PostMapping
    public ResponseEntity<Metrics> create(@RequestBody Metrics metrics) {
        return ResponseEntity.ok(service.createMetric(metrics));
    }

    @GetMapping
    @JsonView({Views.MetricView.class})
    public List<Metrics> findAll() {
        return repository.findAll();
    }

    @JsonView({Views.MetricView.class})
    @GetMapping("/node/{nodeId}")
    public ResponseEntity<List<Metrics>> getByNode(@PathVariable Long nodeId) {
        return ResponseEntity.ok(service.getMetricsByNode(nodeId));
    }

    @JsonView({Views.MetricView.class})
    @GetMapping("/node/{nodeId}/last")
    public ResponseEntity<Metrics> getLast(@PathVariable Long nodeId) {
        return ResponseEntity.ok(service.getLastMetric(nodeId));
    }
}
