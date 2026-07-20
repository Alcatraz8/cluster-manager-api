package portifolio.conteiner_analyzer.DTO.response;

import java.time.LocalDateTime;

public record MetricResponseDTO(
        Long id,
        Double cpuUsage,
        Double memoryUsage,
        Double diskUsage,
        Double networkUsage,
        Double memoryLimit,
        LocalDateTime timestamp,
        Long nodeId
) {
}
