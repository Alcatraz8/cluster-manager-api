package portifolio.conteiner_analyzer.entities;

import jakarta.persistence.*;
import lombok.Data;
import portifolio.conteiner_analyzer.entities.conteiner.Cluster;

import java.util.List;

@Entity
@Data
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String company;

    private String email;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "customer")
    private List<Cluster> clusters;
}
