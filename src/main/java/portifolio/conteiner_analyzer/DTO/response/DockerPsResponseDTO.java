package portifolio.conteiner_analyzer.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DockerPsResponseDTO(
        @JsonProperty("ID") String id,
        @JsonProperty("Names") String name,
        @JsonProperty("Image") String image,
        @JsonProperty("State") String state,
        @JsonProperty("Status") String status,
        @JsonProperty("Ports") String ports,
        @JsonProperty("Networks") String network
) {
}