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

    @JsonView({Views.MetricView.class})
    private Double cpuUsage;

    @JsonView({Views.MetricView.class})
    private Double memoryUsage;

    @JsonView({Views.MetricView.class})
    private Double diskUsage;

    @JsonView({Views.MetricView.class})
    private Double networkUsage;

    @JsonView({Views.MetricView.class})
    private Double memoryLimit;

    @JsonView({Views.MetricView.class})
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "node_id")
    private Node node;
}
