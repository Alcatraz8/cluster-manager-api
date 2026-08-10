package portifolio.conteiner_analyzer.service.ssh;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.springframework.stereotype.Service;
import portifolio.conteiner_analyzer.DTO.response.DockerPsResponseDTO;
import tools.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.EnumSet;
import java.util.List;

@Service
public class SshService {

    private static final Duration CONNECTION_TIMEOUT =
            Duration.ofSeconds(10);

    private static final Duration COMMAND_TIMEOUT =
            Duration.ofSeconds(15);

    private final ObjectMapper objectMapper;

    public SshService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String executeCommand(
            String host,
            int port,
            String username,
            String password,
            String command
    ) {

        SshClient client = SshClient.setUpDefaultClient();
        client.start();

        try (
                ClientSession session = client
                        .connect(username, host, port)
                        .verify(CONNECTION_TIMEOUT)
                        .getSession()
        ) {
            session.addPasswordIdentity(password);
            session.auth().verify(CONNECTION_TIMEOUT);

            try (
                    ByteArrayOutputStream output =
                            new ByteArrayOutputStream();

                    ByteArrayOutputStream errorOutput =
                            new ByteArrayOutputStream();

                    ClientChannel channel =
                            session.createExecChannel(command)
            ) {
                channel.setOut(output);
                channel.setErr(errorOutput);

                channel.open().verify(CONNECTION_TIMEOUT);

                channel.waitFor(
                        EnumSet.of(ClientChannelEvent.CLOSED),
                        COMMAND_TIMEOUT
                );

                String standardOutput =
                        output.toString(StandardCharsets.UTF_8);

                String standardError =
                        errorOutput.toString(StandardCharsets.UTF_8);

                Integer exitStatus = channel.getExitStatus();

                if (exitStatus == null || exitStatus != 0) {
                    throw new RuntimeException(
                            "Remote command failed. Exit status: "
                                    + exitStatus
                                    + ". Error: "
                                    + standardError
                    );
                }

                return standardOutput.trim();
            }

        } catch (Exception exception) {
            throw new RuntimeException(
                    "Could not execute SSH command on host "
                            + host
                            + ": "
                            + exception.getMessage(),
                    exception
            );

        } finally {
            client.stop();
        }
    }

    public List<DockerPsResponseDTO> listContainers(
            String host,
            int port,
            String username,
            String password
    ) {

        String output = executeCommand(
                host,
                port,
                username,
                password,
                "wsl docker ps -a --format \"{{json .}}\""
        );

        System.out.println(output);

        if (output == null || output.isBlank()) {
            return List.of();
        }

        List<DockerPsResponseDTO> containers = output.lines()
                .filter(line -> !line.isBlank())
                .map(this::toDockerPsDTO)
                .toList();

        System.out.println("DTOs: " + containers);

        return containers;
    }

    private DockerPsResponseDTO toDockerPsDTO(String line) {
        try {
            return objectMapper.readValue(
                    line,
                    DockerPsResponseDTO.class
            );

        } catch (Exception exception) {
            throw new RuntimeException(
                    "Could not parse Docker container. JSON: " + line,
                    exception
            );
        }
    }
}