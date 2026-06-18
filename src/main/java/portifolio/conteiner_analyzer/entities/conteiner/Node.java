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

    @JsonView({Views.CustomerView.class, Views.NodeView.class})
    private String name;

    @JsonView({Views.NodeView.class})
    private String ipAddress;

    @JsonView({Views.NodeView.class})
    private String containerId;

    @JsonView({Views.NodeView.class})
    private String image;

    @JsonView({Views.NodeView.class})
    private String ports;

    @JsonView({Views.NodeView.class})
    private String command;

    @JsonView({Views.NodeView.class})
    private LocalDateTime createdAt;

    @JsonView({Views.NodeView.class})
    @Enumerated(EnumType.STRING)
    private NodeStatus status;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "cluster_id")
    @JsonView({Views.NodeView.class})
    private Cluster cluster;

    @OneToMany(mappedBy = "node")
    @JsonView({Views.NodeView.class})
    private List<Metric> metrics;
}