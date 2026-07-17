package portifolio.conteiner_analyzer.DTO.response;

public record CustomerResponseDTO(
        Long id,
        String name,
        String company,
        String email,
        Long userId
) {

}
