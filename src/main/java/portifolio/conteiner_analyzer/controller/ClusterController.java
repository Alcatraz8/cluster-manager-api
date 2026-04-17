package portifolio.conteiner_analyzer.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portifolio.conteiner_analyzer.configuration.Views;
import portifolio.conteiner_analyzer.conteiner.Cluster;
import portifolio.conteiner_analyzer.repository.ClusterRepository;
import portifolio.conteiner_analyzer.service.ClusterService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cluster")
public class ClusterController {

    @Autowired
    public ClusterService service;

    @Autowired
    public ClusterRepository repository;

    @GetMapping
    public List<Cluster> findAll() {
        return repository.findAll();
    }

    @GetMapping("{id}")
    @JsonView(Views.ClusterView.class)
    public Optional<Cluster> findById(@PathVariable Long id) {
        return repository.findById(id);
    }

    @PostMapping("/create/{customerId}")
    public ResponseEntity<String> createCluster(@PathVariable Long customerId,
                                                @RequestBody Cluster cluster) {
        service.createCluster(cluster, customerId);

        return ResponseEntity.ok("Cluster created successfully");
    }
}
