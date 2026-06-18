package portifolio.conteiner_analyzer.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class DockerService {

    public List<String> getStats() {

        List<String> resultList = new ArrayList<>();

        try {

            Process process = new ProcessBuilder(
                    "wsl",
                    "docker",
                    "stats",
                    "--no-stream",
                    "--format",
                    "{{json .}}"
            ).start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream())
            );

            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.out.println("DOCKER ERROR: " + errorLine);
            }

            String line;

            while ((line = reader.readLine()) != null) {
                resultList.add(line);
            }

            int exitCode = process.waitFor();

            System.out.println("EXIT CODE: " + exitCode);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error executing stats",
                    e
            );
        }

        return resultList;
    }
}