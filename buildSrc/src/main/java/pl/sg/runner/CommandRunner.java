package pl.sg.runner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandRunner {
    public static @NotNull ProcessResult runCommand(List<String> command) {
        return runCommand(command, false, null);
    }

    public static @NotNull ProcessResult runCommand(
            List<String> command,
            boolean logToSystemOut,
            @Nullable File workingDirectory) {
        if (logToSystemOut) {
            System.out.println("Running command: " + String.join(" ", command));
        }
        final Process proc;
        try {
            proc = new ProcessBuilder(command)
                    .directory(workingDirectory)
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .redirectError(ProcessBuilder.Redirect.PIPE)
                    .start();
            proc.waitFor();
            List<String> output = read(proc.getInputStream());
            List<String> error = read(proc.getErrorStream());
            if (logToSystemOut) {
                System.out.println("Finished.");
                System.out.println("output: " + String.join("\n", output));
                System.out.println("error: " + String.join("\n", error));
            }
            return new ProcessResult(
                    output,
                    error,
                    proc.exitValue()
            );
        } catch (IOException | InterruptedException e) {
            if (logToSystemOut) {
                System.out.println("Error.");
            }
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
