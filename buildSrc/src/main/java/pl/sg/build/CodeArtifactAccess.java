package pl.sg.build;

import pl.sg.runner.CommandRunner;
import pl.sg.runner.ProcessResult;

import java.util.Objects;

public class CodeArtifactAccess {
    public static String token = null;

    public static synchronized String getToken() {
        if (token == null) {
            token = fetchToken();
        }
        return Objects.requireNonNull(token, "Fetching code artifact token failed");
    }

    private static String fetchToken() {
        String awsCommand = OsUtil.getOS() == OsUtil.OS.MAC
                ? "/opt/homebrew/bin/aws"
                : "aws";
        String awsProfile = OsUtil.getOS() == OsUtil.OS.MAC
                ? "--profile sg-app"
                : "";
        String command = """
                %s codeartifact %s
                get-authorization-token
                --domain sg-repository
                --domain-owner 215372400964
                --region eu-central-1
                --query authorizationToken
                --output text
                """.formatted(awsCommand, awsProfile);
        ProcessResult processResult = CommandRunner.runCommand(command);
        if (processResult.exitCode() > 0) {
            throw new RuntimeException(String.join("\n", processResult.error()));
        }
        if (processResult.output().size() != 1) {
            throw new RuntimeException("Output contains more than one line:\n" + String.join("\n", processResult.output()));
        }
        return processResult.output().getFirst();
    }
}