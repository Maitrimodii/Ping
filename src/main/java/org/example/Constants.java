package org.example;

import java.util.logging.Logger;

public class Constants
{
    protected static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    protected static final boolean IS_WINDOWS = OS_NAME.contains("win");

    protected static final Logger LOGGER = Logger.getLogger(PingUtility.class.getName());

    protected static final String PING_COMMAND = "ping";

    protected static final String PING_FLAGS = IS_WINDOWS ? "-n" : "-c";

    protected static final int PING_COUNT = 5;

    protected static final String TRACEROUTE_COMMAND = IS_WINDOWS ? "tracert" : "traceroute";

    protected static final String NSLOOKUP_COMMAND = "nslookup";

    protected static final String SSH_COMMAND = "ssh";

    protected static final int COMMAND_TIMEOUT_SECONDS = 10;

    protected static final String INVALID_CHOICE = "Invalid choice:  Exiting...";

    //Regex for host validations
    protected static final String HOST_REGEX = "^([a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+|" +
            "((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9]))$";
    protected static final String INVALID_HOST_MESSAGE = "Invalid host: ";

    protected static final String UNSUPPORTED_COMMAND_MESSAGE = "Unsupported command: ";

    protected static final String COMMAND_EXECUTION_MESSAGE = "Executing %s command: %s";

    protected static final String COMMAND_TIMEOUT_MESSAGE = "%s timed out for %s";

    protected static final String COMMAND_FAILED_MESSAGE = "%s failed for %s";

    protected static final String COMMAND_SUCCESS_MESSAGE = "%s successful for %s";

    protected static final String COMMAND_ERROR_MESSAGE = "Error while executing %s for %s";

}
