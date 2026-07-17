package portifolio.conteiner_analyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portifolio.conteiner_analyzer.DTO.request.ClusterRequestDTO;
import portifolio.conteiner_analyzer.DTO.response.ClusterResponseDTO;
import portifolio.conteiner_analyzer.repository.ClusterRepository;
import portifolio.conteiner_analyzer.service.ClusterService;

import java.util.List;

@RestController
@RequestMapping("/cluster")
public class ClusterController {

    @Autowired
    public ClusterService service;

    @Autowired
    public ClusterRepository repository;

    @GetMapping
    public ResponseEntity<List<ClusterResponseDTO>> findAll() {
        return ResponseEntity
                .ok()
                .body(service.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<ClusterResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity
                .ok()
                .body(service.findById(id));
    }

    @PostMapping("/create/{customerId}")
    public ResponseEntity<ClusterResponseDTO> createCluster(@PathVariable Long customerId,
                                                            @RequestBody ClusterRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createCluster(dto, customerId));
    }

}