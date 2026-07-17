package portifolio.conteiner_analyzer.DTO.request;

public record CustomerRequestDTO (
    Long id,
    String name,
    String company,
    String email,
    Long userId
) {

}
