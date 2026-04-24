package portifolio.conteiner_analyzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portifolio.conteiner_analyzer.conteiner.Cluster;
import portifolio.conteiner_analyzer.conteiner.Node;
import portifolio.conteiner_analyzer.repository.ClusterRepository;
import portifolio.conteiner_analyzer.repository.NodeRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class NodeService {

    @Autowired
    private NodeRepository repository;

    @Autowired
    private ClusterRepository clusterRepository;


    public String createNodeContainer(String nodeName) {

        try {

            Process process = new ProcessBuilder(
                    "wsl",
                    "docker",
                    "run",
                    "-d",
                    "--name",
                    nodeName,
                    "nginx"
            ).start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream())
            );

            StringBuilder output = new StringBuilder();
            StringBuilder error = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            while ((line = errorReader.readLine()) != null) {
                error.append(line).append("\n");
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("Container creation error: " + error);
            }

            System.out.println("Container created: " + output);

            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error running docker run", e);
        }
    }


    public Node createNodeInCluster(Long clusterId, String nodeName) {

        Cluster cluster = clusterRepository.findById(clusterId)
                .orElseThrow(() -> new RuntimeException("Cluster not found"));

        try {

            Process process = new ProcessBuilder(
                    "wsl",
                    "docker",
                    "run",
                    "-d",
                    "--name", nodeName,
                    "--network", cluster.getNetworkName(),
                    "nginx"
            ).start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream())
            );

            String containerId = reader.readLine();

            String errorLine;
            StringBuilder error = new StringBuilder();

            while ((errorLine = errorReader.readLine()) != null) {
                error.append(errorLine);
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("Container creation error: " + error);
            }

            Node node = new Node();
            node.setName(nodeName);
            node.setCluster(cluster);
            node.setContainerId(containerId);
            node.setStatus("RUNNING");
            node.setImage("nginx");

            return repository.save(node);

        } catch (Exception e) {
            throw new RuntimeException("Error creating node in cluster", e);
        }
    }

}
