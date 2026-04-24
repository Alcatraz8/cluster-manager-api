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
    private ClusterRepository repository;

    @Autowired
    private CustomerRepository customerRepository;


    public Cluster createCluster(Cluster cluster, Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (cluster.getNetworkName() == null || cluster.getNetworkName().isEmpty()) {
            throw new RuntimeException("Cluster Name is mandatory");
        }

        repository.findByNetworkName(cluster.getNetworkName())
                .ifPresent(c -> {
                    throw new RuntimeException("Cluster already exists");
                });

        String networkName = "cluster_" + cluster.getNetworkName();

        try {
            Process process = new ProcessBuilder(
                    "wsl",
                    "docker",
                    "network",
                    "create",
                    networkName
            ).start();

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("Error creating network");
            }

        } catch (Exception e) {
            throw new RuntimeException("Cluster creation error", e);
        }

        cluster.setCustomer(customer);
        cluster.setNetworkName(networkName);

        return repository.save(cluster);
    }
}

