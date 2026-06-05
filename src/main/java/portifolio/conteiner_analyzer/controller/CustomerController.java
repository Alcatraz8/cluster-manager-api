package portifolio.conteiner_analyzer.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portifolio.conteiner_analyzer.configuration.Views;
import portifolio.conteiner_analyzer.entities.Customer;
import portifolio.conteiner_analyzer.entities.User;
import portifolio.conteiner_analyzer.repository.CustomerRepository;
import portifolio.conteiner_analyzer.repository.UserRepository;
import portifolio.conteiner_analyzer.service.CustomerService;
import portifolio.conteiner_analyzer.service.MetricService;
import portifolio.conteiner_analyzer.service.NodeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerService service;

    @Autowired
    private MetricService metricService;

    @Autowired
    private NodeService nodeService;


    @JsonView(Views.CustomerView.class)
    @GetMapping
    public List<Customer> findAll() {
        return repository.findAll();
    }

    @JsonView({Views.CustomerView.class})
    @GetMapping("/{id}")
    public Optional<Customer> findById(@PathVariable Long id) {
        return repository.findById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer) {
        service.createCustomer(customer);
        return ResponseEntity.ok("Customer created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCustomer(
            @PathVariable Long id,
            @RequestBody Customer updatedCustomer) {


        repository.findById(id)
                .map(customer -> {

                    customer.setName(updatedCustomer.getName());
                    customer.setEmail(updatedCustomer.getEmail());
                    customer.setCompany(updatedCustomer.getCompany());

                    repository.save(customer);

                    return ResponseEntity.ok(customer);
                })
                .orElse(ResponseEntity.notFound().build());

        return ResponseEntity.ok("Customer updated successfull");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id)
    {

        return repository.findById(id)

                .map(customer -> {
                    User user = customer.getUser();

                    if (user != null) {

                        user.setCustomer(null);

                        userRepository.save(user);
                    }

                    repository.delete(customer);

                    return ResponseEntity.ok(
                            "Customer deleted successfully"
                    );
                })
                .orElse(
                        ResponseEntity.notFound().build()
                );
    }
}
