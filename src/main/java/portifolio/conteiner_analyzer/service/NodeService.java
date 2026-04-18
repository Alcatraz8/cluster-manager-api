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

    public Node createNode(Node node) {
        Cluster cluster = clusterRepository.findById(node.getCluster().getId())
                .orElseThrow(() -> new RuntimeException("Cluster not found"));

        node.setCluster(cluster);
        return repository.save(node);
    }


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
                throw new RuntimeException("Erro ao criar container: " + error);
            }

            System.out.println("CONTAINER CRIADO: " + output);

            return output.toString(); // geralmente retorna o container ID

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao executar docker run", e);
        }
    }

}
