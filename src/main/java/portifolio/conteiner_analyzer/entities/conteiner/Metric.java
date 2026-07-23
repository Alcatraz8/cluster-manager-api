package portifolio.conteiner_analyzer.entities.conteiner;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import portifolio.conteiner_analyzer.configuration.Views;

import java.time.LocalDateTime;

@Entity
@Data
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double cpuUsage;

    private Double memoryUsage;

    private Double diskUsage;

    private Double networkUsage;

    private Double memoryLimit;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "node_id")
    private Node node;
}
