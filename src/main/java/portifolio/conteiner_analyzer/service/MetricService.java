package portifolio.conteiner_analyzer.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portifolio.conteiner_analyzer.DTO.response.MetricResponseDTO;
import portifolio.conteiner_analyzer.entities.conteiner.Metric;
import portifolio.conteiner_analyzer.entities.conteiner.Node;
import portifolio.conteiner_analyzer.exception.ResourceNotFoundException;
import portifolio.conteiner_analyzer.repository.MetricRepository;
import portifolio.conteiner_analyzer.repository.NodeRepository;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MetricService {

    @Autowired
    private MetricRepository repository;

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private DockerService dockerService;


    public MetricResponseDTO getLastMetric(long nodeId) {
        Metric metric = repository.findTopByNodeIdOrderByTimestampDesc(nodeId);

        return toResponseDTO(metric);
    }

    public MetricResponseDTO collectMetric(Long nodeId) {

        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Node not found"));

        List<String> statsList = dockerService.getStats();

        String statsJson = null;

        for (String stat : statsList) {

            JsonNode json = parseJson(stat);

            String containerName =
                    json.get("Name").asText();

            if (containerName.equals(node.getName())) {
                statsJson = stat;
                break;
            }
        }

        if (statsJson == null) {
            throw new RuntimeException(
                    "Container stats not found for node "
                            + node.getName()
            );
        }

        try {

            ObjectMapper mapper = new ObjectMapper();

            JsonNode json =
                    mapper.readTree(statsJson);

            Metric metric = new Metric();

            metric.setCpuUsage(
                    parsePercent(
                            json.get("CPUPerc")
                                    .asText()
                    )
            );

            metric.setMemoryUsage(
                    parsePercent(
                            json.get("MemPerc")
                                    .asText()
                    )
            );

            metric.setDiskUsage(
                    parseSize(
                            json.get("BlockIO")
                                    .asText()
                    )
            );

            metric.setNetworkUsage(
                    parseSize(
                            json.get("NetIO")
                                    .asText()
                    )
            );

            metric.setMemoryLimit(
                    extractMemoryLimit(
                            json.get("MemUsage")
                                    .asText()
                    )
            );

            metric.setTimestamp(
                    LocalDateTime.now()
            );

            metric.setNode(node);

            Metric savedMetric = repository.save(metric);

            return toResponseDTO(savedMetric);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error parsing docker stats",
                    e
            );
        }
    }

    public List<MetricResponseDTO> findAll(){
        return repository.findAll()
                .stream().map(this::toResponseDTO)
                .toList();
    }

    public MetricResponseDTO findById(Long id){
        Metric metric = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Metric not found"));
        return toResponseDTO(metric);
    }

    private MetricResponseDTO toResponseDTO(Metric metric){
        return new MetricResponseDTO(
                metric.getId(),
                metric.getCpuUsage(),
                metric.getMemoryUsage(),
                metric.getDiskUsage(),
                metric.getNetworkUsage(),
                metric.getMemoryLimit(),
                metric.getTimestamp(),
                metric.getNode().getId()
        );
    }

    private JsonNode parseJson(String json) {

        try {

            return new ObjectMapper()
                    .readTree(json);

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    private Double parsePercent(String value) {

        return Double.parseDouble(
                value.replace("%", "")
        );
    }

    private Double parseSize(String value) {

        String firstPart =
                value.split("/")[0]
                        .trim()
                        .replace("kB", "")
                        .replace("MB", "")
                        .replace("GB", "")
                        .replace("B", "");

        try {
            return Double.parseDouble(firstPart);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private Double extractMemoryLimit(String memUsage) {

        String[] parts = memUsage.split("/");

        if (parts.length < 2) {
            return 0.0;
        }

        String limit = parts[1]
                .trim()
                .replace("MiB", "")
                .replace("GiB", "");

        try {
            return Double.parseDouble(limit);
        } catch (Exception e) {
            return 0.0;
        }
    }
}