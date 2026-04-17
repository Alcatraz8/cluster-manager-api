package portifolio.conteiner_analyzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portifolio.conteiner_analyzer.conteiner.Cluster;
import portifolio.conteiner_analyzer.conteiner.Node;
import portifolio.conteiner_analyzer.repository.ClusterRepository;
import portifolio.conteiner_analyzer.repository.NodeRepository;

@Service
public class NodeService {

    @Autowired
    private NodeRepository repository;

    @Autowired
    private ClusterRepository clusterRepository;

    public Node createNode(Node node, Long clusterId) {
        Cluster cluster = clusterRepository.findById(clusterId)
                .orElseThrow(() -> new RuntimeException("Cluster not found"));

        node.setCluster(cluster);
        return repository.save(node);
    }
}
