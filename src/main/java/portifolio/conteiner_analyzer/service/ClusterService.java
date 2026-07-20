package portifolio.conteiner_analyzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portifolio.conteiner_analyzer.DTO.request.ClusterRequestDTO;
import portifolio.conteiner_analyzer.DTO.response.ClusterResponseDTO;
import portifolio.conteiner_analyzer.entities.conteiner.Cluster;
import portifolio.conteiner_analyzer.entities.Customer;
import portifolio.conteiner_analyzer.repository.ClusterRepository;
import portifolio.conteiner_analyzer.repository.CustomerRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

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
        cluster.setNickname(dto.nickname());
        cluster.setDescription(dto.description());
        cluster.setNetworkName(networkName);

        Cluster savedCluster = repository.save(cluster);

        return toResponseDTO(savedCluster);
    }

    public ClusterResponseDTO updateCluster(Long id, ClusterRequestDTO dto){

        Cluster cluster = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cluster not found"));

        cluster.setDescription(dto.description());
        cluster.setNickname(dto.nickname());

        Cluster updatedCluster = repository.save(cluster);

        return toResponseDTO(updatedCluster);
    }

    public List<ClusterResponseDTO> findAll(){
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO).toList();
    }

    public ClusterResponseDTO findById(Long id){
        Cluster cluster = repository.findById(id).orElseThrow(() -> new RuntimeException("Cluster not found"));

        return toResponseDTO(cluster);
    }

    public void deleteCluster(Long id){
        Cluster cluster = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cluster not found"));
        if(cluster.getNodes() != null && !cluster.getNodes().isEmpty()){
            throw new RuntimeException(
                    "Cluster cannot be deleted because it contains nodes"
            );
        }

        if (cluster.getNetworkName() == null || cluster.getNetworkName().isBlank()) {
            throw new RuntimeException("Cluster does not have a Docker network name");
        }

        deleteDockerNetwork(cluster.getNetworkName());

        repository.delete(cluster);
    }

    private void deleteDockerNetwork(String networkName) {

        try {
            Process process = new ProcessBuilder(
                    "wsl",
                    "docker",
                    "network",
                    "rm",
                    networkName
            ).redirectErrorStream(true).start();

            String output;

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {

                output = reader.lines()
                        .collect(Collectors.joining(System.lineSeparator()));
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException(
                        "Failed to delete Docker network: " + output
                );
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error while communicating with Docker",
                    e
            );

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "Docker network deletion was interrupted",
                    e
            );
        }
    }

    private ClusterResponseDTO toResponseDTO(Cluster cluster){
        return new ClusterResponseDTO(
                cluster.getId(),
                cluster.getNickname(),
                cluster.getDescription(),
                cluster.getNetworkName(),
                cluster.getCustomer().getId()
        );
    }
}

