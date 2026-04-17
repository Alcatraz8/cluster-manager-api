package portifolio.conteiner_analyzer.conteiner;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double cpuUsage;
    private double memoryUsage;
    private double diskUsage;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "node_id")
    private Node node;
}
