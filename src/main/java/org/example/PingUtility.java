package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.concurrent.TimeUnit;

import static org.example.Constants.*;

public class PingUtility {

    public static void main(String[] args) {

        var scanner = new Scanner(System.in);

        //prompt user to select the command
        System.out.println("Select a command to execute:");

        System.out.println("1. Ping");

        System.out.println("2. Traceroute");

        System.out.println("3. Nslookup");

        System.out.println("4. SSH");

        System.out.println("Enter your choice(1-4): ");

        var choice = scanner.nextInt();
        scanner.nextLine();

        if(choice < 1 || choice > 4)
        {
            LOGGER.warning("Invalid choice. Exiting...");
            return;
        }

        System.out.println("Enter the host(s) to execute the command on (space separated): ");

        var hostsInput = scanner.nextLine();

        String[] hosts = hostsInput.split("\\s+");

        for (var host : hosts)
        {

            if(!isValidateHost(host))
            {
                LOGGER.warning(String.format(INVALID_HOST_MESSAGE, host));

                continue;
            }

            executeCommand(choice, host);
        }

        scanner.close();
    }

    private static void executeCommand(int choice, String host)
    {
        switch (choice)
        {
            case 1:
                ping(host);
                break;

            case 2:
                traceroute(host);
                break;

            case 3:
                nslookup(host);
                break;

            case 4:
                ssh(host);
                break;

            default:
                LOGGER.warning(UNSUPPORTED_COMMAND_MESSAGE + choice);
        }
    }

    /**
     * Validate host using a regex pattern
     * @param host
     * @return
     */
    private static boolean isValidateHost(String host)
    {
        return host.matches(HOST_REGEX);
    }

    /**
     * Ping command for given host
     * @param host
     */
    private static void ping(String host)
    {
        var command = new ArrayList<String>(Arrays.asList(PING_COMMAND, PING_FLAGS, String.valueOf(PING_COUNT), host));

        executeCommand("ping", command, host);
    }

    /**
     * Traceroute command for given host
     * @param host
     */
    private static void traceroute(String host)
    {
        var command = new ArrayList<String>(Arrays.asList(TRACEROUTE_COMMAND, host));

        executeCommand("traceroute", command, host);
    }

    /**
     * Nslookup command for given host
     * @param host
     */
    private static void nslookup(String host)
    {
        var command = new ArrayList<String>(Arrays.asList(NSLOOKUP_COMMAND, host));

        executeCommand("nslookup", command, host);
    }

    /**
     * SSH command for given host
     * @param host
     */
    private static void ssh(String host)
    {
        var command = new ArrayList<String>(Arrays.asList(SSH_COMMAND, host));

        executeCommand("ssh", command, host);
    }

    /**
     * Execute the command and print the output
     * @param commandName   The name of command(eg. ping, traceroute, nslookup, ssh)
     * @param command       The command to execute as list of strings
     * @param host
     */
    private static void executeCommand(String commandName, List<String> command, String host){

        try {
            var processBuilder = new ProcessBuilder(command);

            processBuilder.redirectErrorStream(true); // Merge error output with normal output

            //start the process
            var process = processBuilder.start();

            LOGGER.info(String.format(COMMAND_EXECUTION_MESSAGE, commandName, String.join(" ", command)));

            // Read and print process output
            try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream())))
            {
                String line;
                while ((line = reader.readLine()) != null)
                {
                    System.out.println(line);
                }
            }

            // Wait for the process to complete within specified timeout
            var completed = process.waitFor(COMMAND_TIMEOUT_SECONDS, TimeUnit.SECONDS);

            //handle process completion
            if (!completed)
            {
                LOGGER.warning(String.format(COMMAND_TIMEOUT_MESSAGE, commandName, host));
                process.destroy();
            }
            else
            {

                var success = process.exitValue() == 0;

                if(success)
                {
                    LOGGER.info(String.format(COMMAND_SUCCESS_MESSAGE, commandName, host));
                }
                else
                {
                    LOGGER.warning(String.format(COMMAND_FAILED_MESSAGE, commandName, host));
                }
            }

        }
        catch (Exception exception)
        {
            LOGGER.log(Level.SEVERE, String.format(COMMAND_ERROR_MESSAGE, commandName, host), exception);
        }
    }
}
