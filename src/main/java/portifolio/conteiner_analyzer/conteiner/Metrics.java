package portifolio.conteiner_analyzer.conteiner;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import portifolio.conteiner_analyzer.configuration.Views;

import java.time.LocalDateTime;

@Entity
@Data
public class Metrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView({Views.MetricView.class})
    private double cpuUsage;
    @JsonView({Views.MetricView.class})
    private double memoryUsage;
    @JsonView({Views.MetricView.class})
    private double diskUsage;

    @JsonView({Views.MetricView.class})
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "node_id")
    private Node node;
}
