package portifolio.conteiner_analyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portifolio.conteiner_analyzer.DTO.response.NodeResponseDTO;
import portifolio.conteiner_analyzer.repository.NodeRepository;
import portifolio.conteiner_analyzer.service.NodeService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/node")
public class NodeController {

    @Autowired
    private NodeService service;

    @Autowired
    private NodeRepository repository;

    @GetMapping
    public ResponseEntity<List<NodeResponseDTO>> findAll() {

        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{nodeId}")
    public ResponseEntity<NodeResponseDTO> findById(@PathVariable Long nodeId) {
        service.refreshNodeInfo(nodeId);
        return ResponseEntity.ok().body(service.findById(nodeId));
    }

    @PostMapping("/customer/{customerId}/node")
    public ResponseEntity<NodeResponseDTO> createNodeContainer(@PathVariable Long customerId,
            @RequestBody Map<String, String> body) {
       return ResponseEntity.ok().body(service.createNodeContainer(customerId, body.get("name")));
    }

    @PostMapping("/cluster/{clusterId}")
    public ResponseEntity<NodeResponseDTO> createNodeInCluster(@PathVariable Long clusterId,
                                                      @RequestBody Map<String, String> body) {
        return ResponseEntity.ok().body(service.createNodeInCluster(clusterId, body.get("name")));
    }

    @DeleteMapping("/{nodeId}")
    public ResponseEntity<Void> deleteNode(@PathVariable Long nodeId) {

        service.deleteNode(nodeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
