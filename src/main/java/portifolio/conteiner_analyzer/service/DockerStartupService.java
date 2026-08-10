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

        Process nodeProcess = new ProcessBuilder(
                "wsl",
                "docker",
                "ps",
                "-a"
        ).start();

        BufferedReader nodeReader = new BufferedReader(
                new InputStreamReader(nodeProcess.getInputStream())
        );

        BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(nodeProcess.getErrorStream())
        );

        String line;

        System.out.println("\n--- DOCKER OUTPUT ---");

        while ((line = nodeReader.readLine()) != null) {

            System.out.println(line);
        }

        System.out.println("\n--- ERRORS ---");

        while ((line = errorReader.readLine()) != null) {

            System.out.println(line);
        }

        nodeProcess.waitFor();

        System.out.println("========================\n");


    System.out.println("\n===== DOCKER Networks =====");

    Process clusterProcess = new ProcessBuilder(
            "wsl",
            "docker",
            "network",
            "ls"
    ).start();

    BufferedReader clusterReader = new BufferedReader(
            new InputStreamReader(clusterProcess.getInputStream())
    );

    BufferedReader clusterErrorReader = new BufferedReader(
            new InputStreamReader(clusterProcess.getErrorStream())
    );

    String clusterLine;

        System.out.println("\n--- DOCKER OUTPUT ---");

        while ((clusterLine = clusterReader.readLine()) != null) {

        System.out.println(clusterLine);
    }

        System.out.println("\n--- ERRORS ---");

        while ((clusterLine = clusterErrorReader.readLine()) != null) {

        System.out.println(clusterLine);
    }

        clusterProcess.waitFor();

        System.out.println("========================\n");
}
}