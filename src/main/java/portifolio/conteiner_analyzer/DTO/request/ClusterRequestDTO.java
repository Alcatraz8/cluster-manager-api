package portifolio.conteiner_analyzer.DTO.request;

public record ClusterRequestDTO(
        String nickname,
        String description,
        String networkName,
        Long customerId
) {

}
