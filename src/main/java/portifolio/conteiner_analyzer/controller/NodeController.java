package portifolio.conteiner_analyzer.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portifolio.conteiner_analyzer.configuration.Views;
import portifolio.conteiner_analyzer.entities.conteiner.Node;
import portifolio.conteiner_analyzer.repository.NodeRepository;
import portifolio.conteiner_analyzer.service.NodeService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/nodes")
public class NodeController {

    @Autowired
    private NodeService service;

    @Autowired
    private NodeRepository repository;

    @GetMapping
    public List<Node> findAll() {
      return repository.findAll();
    }

    @JsonView({Views.NodeView.class})
    @GetMapping("/{nodeId}")
    public Optional<Node> findById(@PathVariable Long nodeId) {
        service.refreshNodeInfo(nodeId);
        return repository.findById(nodeId);
    }

    @JsonView({Views.NodeView.class})
    @PostMapping("/customer/{customerId}/node")
    public ResponseEntity<String> createNodeContainer(@PathVariable Long customerId,
            @RequestBody Map<String, String> body) {
       service.createNodeContainer(customerId, body.get("name"));

        return ResponseEntity.ok("Node created successfully");
    }

    @JsonView({Views.NodeView.class})
    @PostMapping("/cluster/{clusterId}")
    public ResponseEntity<String> createNodeInCluster(@PathVariable Long clusterId,
                                                      @RequestBody Map<String, String> body) {
        service.createNodeInCluster(clusterId, body.get("name"));

        return ResponseEntity.ok("Node created successfully");
    }

    @DeleteMapping("/{nodeId}")
    public ResponseEntity<String> deleteNode(@PathVariable Long nodeId) {
        service.deleteNode(nodeId);
        repository.deleteById(nodeId);
        return ResponseEntity.ok("Node deleted successfull");
    }

}
