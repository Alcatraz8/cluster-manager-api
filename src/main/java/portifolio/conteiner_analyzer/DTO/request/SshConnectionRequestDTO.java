package portifolio.conteiner_analyzer.DTO.request;

public record SshConnectionRequestDTO(
        String host,
        Integer port,
        String username,
        String password
) {
}
