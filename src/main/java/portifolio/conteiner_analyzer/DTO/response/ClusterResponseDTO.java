package portifolio.conteiner_analyzer.DTO.response;

public record ClusterResponseDTO(
        Long id,
        String nickname,
        String description,
        String networkName,
        Long customerId
) {

}
