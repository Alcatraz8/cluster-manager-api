package portifolio.conteiner_analyzer.entities;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import portifolio.conteiner_analyzer.configuration.Views;
import portifolio.conteiner_analyzer.conteiner.Cluster;
import portifolio.conteiner_analyzer.conteiner.Node;

import java.util.List;

@Entity
@Data
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonView({Views.UserView.class, Views.CustomerView.class})
    private String name;
    @JsonView({Views.UserView.class, Views.CustomerView.class})
    private String company;
    @JsonView({Views.UserView.class, Views.CustomerView.class})
    private String email;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonView(Views.CustomerView.class)
    @OneToMany(mappedBy = "customer")
    private List<Cluster> clusters;
}
