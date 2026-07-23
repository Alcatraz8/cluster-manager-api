package portifolio.conteiner_analyzer.entities.conteiner;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import portifolio.conteiner_analyzer.configuration.Views;
import portifolio.conteiner_analyzer.entities.Customer;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "nodes")
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String ipAddress;

    private String containerId;

    private String image;

    private String ports;

    private String command;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private NodeStatus status;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "cluster_id")
    private Cluster cluster;

    @OneToMany(mappedBy = "node")
    private List<Metric> metrics;
}