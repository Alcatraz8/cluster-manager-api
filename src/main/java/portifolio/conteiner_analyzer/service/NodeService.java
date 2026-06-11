package portifolio.conteiner_analyzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portifolio.conteiner_analyzer.entities.Customer;
import portifolio.conteiner_analyzer.entities.conteiner.Cluster;
import portifolio.conteiner_analyzer.entities.conteiner.Node;
import portifolio.conteiner_analyzer.repository.ClusterRepository;
import portifolio.conteiner_analyzer.repository.CustomerRepository;
import portifolio.conteiner_analyzer.repository.NodeRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class NodeService {

    @Autowired
    private NodeRepository repository;

    @Autowired
    private ClusterRepository clusterRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Node createNodeContainer(Long customerId, String nodeName) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));

        String containerId = createDockerContainer(nodeName, null);

        Node node = new Node();

        node.setName(nodeName);
        node.setContainerId(containerId);
        node.setStatus("RUNNING");
        node.setImage("nginx");

        node.setCustomer(customer);
        node.setCluster(null);

        return repository.save(node);
    }

    public Node createNodeInCluster(Long clusterId, String nodeName) {

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
        node.setStatus("RUNNING");
        node.setImage("nginx");

        node.setCluster(cluster);
        node.setCustomer(cluster.getCustomer());

        return repository.save(node);
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
}