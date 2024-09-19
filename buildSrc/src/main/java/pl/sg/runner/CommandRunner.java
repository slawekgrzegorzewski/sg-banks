package pl.sg.runner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandRunner {
    public static ProcessResult runCommand(List<String> command) {
        System.out.println("Running command: " + String.join(" ", command));
        final Process proc;
        try {
            proc = new ProcessBuilder(command)
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .redirectError(ProcessBuilder.Redirect.PIPE)
                    .start();
            proc.waitFor();
            System.out.println("Finished.");
            return new ProcessResult(
                    read(proc.getInputStream()),
                    read(proc.getErrorStream()),
                    proc.exitValue()
            );
        } catch (IOException | InterruptedException e) {
            System.out.println("Error.");
            return new ProcessResult(
                    List.of(),
                    List.of(
                            "Exception while executing command: " + command,
                            e.getMessage(),
                            Arrays.stream(e.getStackTrace())
                                    .map(StackTraceElement::toString)
                                    .collect(Collectors.joining("\n"))),
                    1
            );
        }
    }

    private static List<String> read(InputStream inputStream) throws IOException {
        try (
                InputStreamReader in = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(in)
        ) {
            return br.lines().toList();
        }
    }
}
