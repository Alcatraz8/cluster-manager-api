package portifolio.conteiner_analyzer.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class DockerStartupService implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        System.out.println("\n===== DOCKER NODES =====");

        Process process = new ProcessBuilder(
                "wsl",
                "docker",
                "ps",
                "-a"
        ).start();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );

        BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(process.getErrorStream())
        );

        String line;

        System.out.println("\n--- DOCKER OUTPUT ---");

        while ((line = reader.readLine()) != null) {

            System.out.println(line);
        }

        System.out.println("\n--- ERRORS ---");

        while ((line = errorReader.readLine()) != null) {

            System.out.println(line);
        }

        process.waitFor();

        System.out.println("========================\n");
    }
}