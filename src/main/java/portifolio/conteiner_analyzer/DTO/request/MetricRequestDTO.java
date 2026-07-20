package portifolio.conteiner_analyzer.DTO.request;

import java.time.LocalDateTime;

public record MetricRequestDTO(
        Double cpuUsage,
        Double memoryUsage,
        Double diskUsage,
        Double networkUsage,
        Double memoryLimit,
        LocalDateTime timestamp,
        Long nodeId
) {
}
