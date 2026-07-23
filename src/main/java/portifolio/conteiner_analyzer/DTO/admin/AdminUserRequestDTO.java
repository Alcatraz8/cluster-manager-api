package portifolio.conteiner_analyzer.DTO.admin;

import portifolio.conteiner_analyzer.entities.Role;

public record AdminUserRequestDTO (
        String login,
        String password,
        Role role
) {
}
