package portifolio.conteiner_analyzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portifolio.conteiner_analyzer.DTO.request.ClusterRequestDTO;
import portifolio.conteiner_analyzer.DTO.response.ClusterResponseDTO;
import portifolio.conteiner_analyzer.entities.conteiner.Cluster;
import portifolio.conteiner_analyzer.entities.Customer;
import portifolio.conteiner_analyzer.repository.ClusterRepository;
import portifolio.conteiner_analyzer.repository.CustomerRepository;

import java.util.List;

@Service
public class ClusterService {

    @Autowired
    private ClusterRepository repository;

    @Autowired
    private CustomerRepository customerRepository;


    public ClusterResponseDTO createCluster(ClusterRequestDTO dto, Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (dto.networkName() == null || dto.networkName().isBlank()) {
            throw new RuntimeException("Cluster name is mandatory");
        }

        String networkName = "cluster_" + dto.networkName();

        if (repository.findByNetworkName(networkName).isPresent()) {
            throw new RuntimeException("Cluster already exists");
        }

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

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Cluster creation interrupted", e);

        } catch (Exception e) {
            throw new RuntimeException("Cluster creation error", e);
        }

        Cluster cluster = new Cluster();
        cluster.setCustomer(customer);
        cluster.setNetworkName(networkName);

        Cluster savedCluster = repository.save(cluster);

        return new ClusterResponseDTO(
                savedCluster.getId(),
                savedCluster.getNetworkName(),
                savedCluster.getCustomer().getId()
        );
    }

    public List<ClusterResponseDTO> findAll(){
        return repository.findAll()
                .stream()
                .map(cluster -> new ClusterResponseDTO(
                        cluster.getId(),
                        cluster.getNetworkName(),
                        cluster.getCustomer().getId()
                        )).toList();
    }

    public ClusterResponseDTO findById(Long id){
        Cluster cluster = repository.findById(id).orElseThrow(() -> new RuntimeException("Cluster not found"));

        return new ClusterResponseDTO(
          cluster.getId(),
          cluster.getNetworkName(),
          cluster.getCustomer().getId()
        );
    }
}

