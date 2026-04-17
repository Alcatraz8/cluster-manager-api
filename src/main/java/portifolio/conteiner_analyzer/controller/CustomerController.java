package portifolio.conteiner_analyzer.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portifolio.conteiner_analyzer.configuration.Views;
import portifolio.conteiner_analyzer.conteiner.Metric;
import portifolio.conteiner_analyzer.entities.Customer;
import portifolio.conteiner_analyzer.repository.CustomerRepository;
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
    private CustomerService service;

    @Autowired
    private MetricService metricService;

    @Autowired
    private NodeService nodeService;


    @PostMapping("/create")
    public ResponseEntity<String> createCustomer (@RequestBody Customer customer) {
        service.createCustomer(customer);
        return ResponseEntity.ok("Customer created successfully");
    }

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

    @PostMapping("/metrics")
    public ResponseEntity<?> saveMetric(@RequestBody Metric metric) {
        metricService.saveMetric(metric);
        return ResponseEntity.ok().build();
    }
}
