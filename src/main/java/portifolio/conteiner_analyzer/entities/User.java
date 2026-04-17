package portifolio.conteiner_analyzer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import portifolio.conteiner_analyzer.configuration.Views;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonView(Views.UserView.class)
    private String username;

    private String password;

    @JsonView(Views.UserView.class)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Customer customer;

}
