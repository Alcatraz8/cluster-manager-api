package portifolio.conteiner_analyzer.conteiner;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import portifolio.conteiner_analyzer.configuration.Views;

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
    @JsonView({Views.CustomerView.class, Views.NodeView.class})
    private String ip;

    @JsonView({Views.NodeView.class})
    private String containerId;

    @JsonView({Views.NodeView.class})
    private String status;
    
    @JsonView({Views.NodeView.class})
    private String image;

    @ManyToOne
    @JsonView({Views.NodeView.class})
    @JoinColumn(name = "cluster_id")
    private Cluster cluster;

    @JsonView({Views.NodeView.class})
    @OneToMany(mappedBy = "node")
    private List<Metrics> metrics;
}
