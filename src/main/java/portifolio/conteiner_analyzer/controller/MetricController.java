package portifolio.conteiner_analyzer.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portifolio.conteiner_analyzer.configuration.Views;
import portifolio.conteiner_analyzer.entities.conteiner.Metric;
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
    public ResponseEntity<Metric> create(@RequestBody Metric metric) {
        return ResponseEntity.ok(service.createMetric(metric));
    }

    @GetMapping
    @JsonView({Views.MetricView.class})
    public List<Metric> findAll() {
        return repository.findAll();
    }

    @JsonView({Views.MetricView.class})
    @GetMapping("/node/{nodeId}/last")
    public ResponseEntity<Metric> getLast(@PathVariable Long nodeId) {
        return ResponseEntity.ok(service.getLastMetric(nodeId));
    }

    @JsonView({Views.MetricView.class})
    @PostMapping("/collect/{nodeId}")
    public ResponseEntity<Metric> collect(@PathVariable Long nodeId){
        return ResponseEntity.ok(service.collectMetric(nodeId));
    }
}
