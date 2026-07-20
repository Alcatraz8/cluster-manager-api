package portifolio.conteiner_analyzer.DTO.response;

import portifolio.conteiner_analyzer.entities.conteiner.NodeStatus;

import java.time.LocalDateTime;

public record NodeResponseDTO(
        Long id,
        String name,
        String ipAddress,
        String containerId,
        String image,
        String ports,
        String command,
        LocalDateTime createdAt,
        NodeStatus status,
        Long customerId,
        Long clusterId
) {
}
