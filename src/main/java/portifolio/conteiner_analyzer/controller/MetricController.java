package portifolio.conteiner_analyzer.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portifolio.conteiner_analyzer.DTO.response.MetricResponseDTO;
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

    @GetMapping
    public ResponseEntity<List<MetricResponseDTO>> findAll(){

        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/node/{metricId}")
    public ResponseEntity<MetricResponseDTO> findById(@PathVariable Long metricId){
        return ResponseEntity.ok().body(service.findById(metricId));
    }

    @GetMapping("/node/{nodeId}/last")
    public ResponseEntity<MetricResponseDTO> getLast(@PathVariable Long nodeId) {
        return ResponseEntity.ok().body(service.getLastMetric(nodeId));
    }

    @PostMapping("/collect/{nodeId}")
    public ResponseEntity<MetricResponseDTO> collect(@PathVariable Long nodeId){
        return ResponseEntity.ok().body(service.collectMetric(nodeId));
    }
}
