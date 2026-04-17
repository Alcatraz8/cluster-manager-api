package portifolio.conteiner_analyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portifolio.conteiner_analyzer.conteiner.Node;
import portifolio.conteiner_analyzer.service.NodeService;

@RestController
@RequestMapping("/nodes")
public class NodeController {

    @Autowired
    private NodeService service;

    @PostMapping("/{clusterId}")
    public ResponseEntity<String> createNode(@PathVariable Long clusterId, @RequestBody Node node) {
        service.createNode(node, clusterId);
        return ResponseEntity.ok("Node created successfully");
    }
}
