package portifolio.conteiner_analyzer.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portifolio.conteiner_analyzer.configuration.Views;
import portifolio.conteiner_analyzer.service.NodeService;

import java.util.Map;

@RestController
@RequestMapping("/nodes")
public class NodeController {

    @Autowired
    private NodeService service;

    @JsonView({Views.NodeView.class})
    @PostMapping("/create-node")
    public ResponseEntity<String> createNodeContainer(@RequestBody Map<String, String> body) {
        service.createNodeContainer(body.get("name"));
        return ResponseEntity.ok("Node created successfully");
    }

    @JsonView({Views.NodeView.class})
    @PostMapping("/{clusterId}/nodes")
    public ResponseEntity<String> createNode(@PathVariable Long clusterId,
                                             @RequestParam String name) {
        service.createNodeInCluster(clusterId, name);
        return ResponseEntity.ok("Node created successfully");
    }
}
