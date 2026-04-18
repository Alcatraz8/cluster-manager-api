package portifolio.conteiner_analyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import portifolio.conteiner_analyzer.service.DockerService;

import java.util.List;

@RestController
@RequestMapping("/docker")
public class DockerController {

    @Autowired
    private DockerService service;


    @GetMapping("/stats")
    public List<String> stats() {
        return service.getStats();
    }
}
