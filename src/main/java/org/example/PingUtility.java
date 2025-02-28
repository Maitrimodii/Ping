package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.example.Constants.*;
import java.util.logging.Level;

public class PingUtility {

    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            LOGGER.warning("No host provided. Usage: java PingUtility <host1> <host2> ...");
            return;
        }

        for (var host : args)
        {
            ping(host);
        }
    }

    private static void ping(String host) {
        var command = new ArrayList<String>();

        // Detect OS and choose appropriate ping command
        if (OS_NAME.contains("win"))
        {
            command.addAll(Arrays.asList("ping", "-n", "4", host)); // Windows
        }
        else
        {
            command.addAll(Arrays.asList("ping", "-c", "4", host)); // Linux/macOS
        }

        try
        {
            var processBuilder = new ProcessBuilder(command);

            processBuilder.redirectErrorStream(true); // Merge error output with normal output

            var process = processBuilder.start();

            LOGGER.info("Executing ping command: " + String.join(" ", command));

            // Read and print process output
            try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream())))
            {
                String line;
                while ((line = reader.readLine()) != null)
                {
                    System.out.println(line);
                }
            }

            // Wait for the process to complete
            var completed = process.waitFor() == 0;
            if (!completed)
            {
                LOGGER.warning("Ping failed for " + host);
            }
            else
            {
                LOGGER.info("Ping successful for " + host);
            }
        }

        catch (Exception exception)
        {
            LOGGER.log(Level.SEVERE, "Error while pinging " + host, exception);
        }
    }
}
