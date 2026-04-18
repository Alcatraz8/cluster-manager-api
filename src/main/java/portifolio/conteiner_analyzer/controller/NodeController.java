package portifolio.conteiner_analyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portifolio.conteiner_analyzer.conteiner.Node;
import portifolio.conteiner_analyzer.service.NodeService;

import java.util.Map;

@RestController
@RequestMapping("/nodes")
public class NodeController {

    @Autowired
    private NodeService service;

    @PostMapping("/create")
    public ResponseEntity<String> createNode(@RequestBody Node node) {
        service.createNode(node);
        return ResponseEntity.ok("Node created successfully");
    }

    @PostMapping("/create-node")
    public String createNodeContainer(@RequestBody Map<String, String> body) {
        return service.createNodeContainer(body.get("name"));
    }
}
