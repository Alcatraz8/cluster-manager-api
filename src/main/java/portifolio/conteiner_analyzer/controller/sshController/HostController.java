package portifolio.conteiner_analyzer.controller.sshController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portifolio.conteiner_analyzer.DTO.request.SshConnectionRequestDTO;
import portifolio.conteiner_analyzer.DTO.response.DockerPsResponseDTO;
import portifolio.conteiner_analyzer.service.ssh.SshService;

import java.util.List;

@RestController
@RequestMapping("/hosts")
public class HostController {

    private final SshService sshService;

    public HostController(SshService sshService) {
        this.sshService = sshService;
    }

    @PostMapping("/test-connection")
    public ResponseEntity<String> testConnection(
            @RequestBody SshConnectionRequestDTO dto
    ) {
        String response = sshService.executeCommand(
                dto.host(),
                dto.port(),
                dto.username(),
                dto.password(),
                "wsl docker ps -a --format \"{{json .}}\""
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/containers")
    public ResponseEntity<List<DockerPsResponseDTO>> listContainers(
            @RequestBody SshConnectionRequestDTO dto
    ) {

        List<DockerPsResponseDTO> containers =
                sshService.listContainers(
                        dto.host(),
                        dto.port(),
                        dto.username(),
                        dto.password()
                );

        System.out.println("Containers retornados: " + containers);

        return ResponseEntity.ok(containers);
    }
}