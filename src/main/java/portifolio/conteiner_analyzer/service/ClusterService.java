package portifolio.conteiner_analyzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portifolio.conteiner_analyzer.conteiner.Cluster;
import portifolio.conteiner_analyzer.entities.Customer;
import portifolio.conteiner_analyzer.repository.ClusterRepository;
import portifolio.conteiner_analyzer.repository.CustomerRepository;

@Service
public class ClusterService {

    @Autowired
    public ClusterRepository repository;

    @Autowired
    public CustomerRepository customerRepository;

    public Cluster createCluster(Cluster cluster, Long customerId) {
        Customer customer = customerRepository.findById(customerId).
                orElseThrow(() -> new RuntimeException("Customer not found"));

        cluster.setCustomer(customer);
        return repository.save(cluster);
    }
}
