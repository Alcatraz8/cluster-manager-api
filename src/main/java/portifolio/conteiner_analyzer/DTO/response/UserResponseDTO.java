package portifolio.conteiner_analyzer.DTO.response;


import portifolio.conteiner_analyzer.entities.Role;

public record UserResponseDTO (
        Long id,
        String login,
        Role role
){

}
