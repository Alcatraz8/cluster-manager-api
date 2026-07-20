package portifolio.conteiner_analyzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portifolio.conteiner_analyzer.DTO.response.NodeResponseDTO;
import portifolio.conteiner_analyzer.entities.Customer;
import portifolio.conteiner_analyzer.entities.conteiner.Cluster;
import portifolio.conteiner_analyzer.entities.conteiner.Node;
import portifolio.conteiner_analyzer.entities.conteiner.NodeStatus;
import portifolio.conteiner_analyzer.repository.ClusterRepository;
import portifolio.conteiner_analyzer.repository.CustomerRepository;
import portifolio.conteiner_analyzer.repository.NodeRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class NodeService {

    @Autowired
    private NodeRepository repository;

    @Autowired
    private ClusterRepository clusterRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public NodeResponseDTO createNodeContainer(Long customerId, String nodeName) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));

        String containerId = createDockerContainer(nodeName, null);

        Node node = new Node();

        node.setName(nodeName);
        node.setContainerId(containerId);
        node.setCustomer(customer);
        node.setCluster(null);

        populateDockerInfo(node);

        Node savedNode = repository.save(node);

        return toResponseDTO(savedNode);
    }

    public NodeResponseDTO createNodeInCluster(Long clusterId, String nodeName) {

        Cluster cluster = clusterRepository.findById(clusterId)
                .orElseThrow(() ->
                        new RuntimeException("Cluster not found"));

        String containerId = createDockerContainer(
                nodeName,
                cluster.getNetworkName()
        );

        Node node = new Node();

        node.setName(nodeName);
        node.setContainerId(containerId);

        node.setCluster(cluster);
        node.setCustomer(cluster.getCustomer());

        populateDockerInfo(node);

        Node savedNode = repository.save(node);

        return toResponseDTO(savedNode);
    }

    public void deleteNode(Long nodeId) {

        Node node = repository.findById(nodeId)
                .orElseThrow(() ->
                        new RuntimeException("Node not found"));

        removeDockerContainer(node.getContainerId());

        repository.delete(node);
    }

    public List<NodeResponseDTO> findAll(){
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public NodeResponseDTO findById(Long nodeId){
        Node node = repository.findById(nodeId)
                .orElseThrow(() -> new RuntimeException("Node not found"));
        return toResponseDTO(node);
    }

    private String createDockerContainer(
            String nodeName,
            String networkName) {

        try {

            ProcessBuilder builder;

            if (networkName == null) {

                builder = new ProcessBuilder(
                        "wsl",
                        "docker",
                        "run",
                        "-d",
                        "--name",
                        nodeName,
                        "nginx"
                );

            } else {

                builder = new ProcessBuilder(
                        "wsl",
                        "docker",
                        "run",
                        "-d",
                        "--name",
                        nodeName,
                        "--network",
                        networkName,
                        "nginx"
                );
            }

            Process process = builder.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream())
            );

            String containerId = reader.readLine();

            StringBuilder error = new StringBuilder();
            String line;

            while ((line = errorReader.readLine()) != null) {
                error.append(line);
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException(
                        "Container creation error: " + error
                );
            }

            System.out.println("Container created: " + containerId);

            return containerId;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error creating Docker container",
                    e
            );
        }
    }

    private void populateDockerInfo(Node node) {

        node.setImage(
                getDockerInfo(
                        node.getContainerId(),
                        "{{.Config.Image}}"
                )
        );

        node.setIpAddress(
                getDockerInfo(
                        node.getContainerId(),
                        "{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}"
                )
        );

        node.setCommand(
                getDockerInfo(
                        node.getContainerId(),
                        "{{.Path}}"
                )
        );

        String status = getDockerInfo(
                node.getContainerId(),
                "{{.State.Status}}"
        );

        if (status != null) {
            node.setStatus(mapDockerStatus(status));
        }

        String created = getDockerInfo(
                node.getContainerId(),
                "{{.Created}}"
        );

        if (created != null) {

            LocalDateTime createdAt =
                    OffsetDateTime.parse(created)
                            .toLocalDateTime();

            node.setCreatedAt(createdAt);
        }
    }

    private String getDockerInfo(
            String containerId,
            String format) {

        try {

            Process process = new ProcessBuilder(
                    "wsl",
                    "docker",
                    "inspect",
                    "-f",
                    format,
                    containerId
            ).start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            process.getInputStream()
                    )
            );

            String result = reader.readLine();

            process.waitFor();

            return result;

        } catch (Exception e) {
            return null;
        }
    }

    private NodeStatus mapDockerStatus(String status) {

        return switch (status.toLowerCase()) {

            case "running" -> NodeStatus.RUNNING;

            case "exited" -> NodeStatus.STOPPED;

            case "paused" -> NodeStatus.PAUSED;

            case "restarting" -> NodeStatus.RESTARTING;

            default -> NodeStatus.STOPPED;
        };
    }

    public NodeResponseDTO refreshNodeInfo(Long nodeId) {

        Node node = repository.findById(nodeId)
                .orElseThrow(() ->
                        new RuntimeException("Node not found"));

        populateDockerInfo(node);

        Node updatedNode = repository.save(node);

        return toResponseDTO(updatedNode);
    }

    private void removeDockerContainer(String containerId) {

        try {

            Process process = new ProcessBuilder(
                    "wsl",
                    "docker",
                    "rm",
                    "-f",
                    containerId
            ).start();

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException(
                        "Error removing container"
                );
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error removing container",
                    e
            );
        }
    }

    private NodeResponseDTO toResponseDTO(Node node){
        return new NodeResponseDTO(
                node.getId(),
                node.getName(),
                node.getIpAddress(),
                node.getContainerId(),
                node.getImage(),
                node.getPorts(),
                node.getCommand(),
                node.getCreatedAt(),
                node.getStatus(),
                node.getCustomer().getId(),
                node.getCluster() != null ? node.getCluster().getId() : null
        );
    }
}