package pl.sg.release;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.sg.runner.CommandRunner;
import pl.sg.runner.ProcessResult;

import java.io.File;
import java.util.List;

public class VersionUtil {

    private VersionUtil() {
    }

    public static String getNextMajorVersion(@Nullable File workingDir) {
        Version version = getCurrentVersion(workingDir);
        return new Version(version.major() + 1, 0, 0).toString();
    }

    public static String getNextMinorVersion(@Nullable File workingDir) {
        Version version = getCurrentVersion(workingDir);
        return new Version(version.major(), version.minor() + 1, 0).toString();
    }

    public static String getNextPatchVersion(@Nullable File workingDir) {
        Version version = getCurrentVersion(workingDir);
        return new Version(version.major(), version.minor(), version.patch() + 1).toString();
    }

    public static String getNextVersion(@Nullable File workingDir, String level) {
        return switch (level) {
            case "patch" -> getNextPatchVersion(workingDir);
            case "minor" -> getNextMinorVersion(workingDir);
            case "major" -> getNextMajorVersion(workingDir);
            default -> throw new IllegalArgumentException("Unknown level: " + level);
        };
    }

    public static @NotNull Version getCurrentVersion(@Nullable File workingDir) {
        System.out.println("workingDir = " + workingDir);
        ProcessResult processResult = CommandRunner.runCommand(List.of("git", "tag", "-l"), true, workingDir);
        if (processResult.exitCode() != 0) {
            throw new RuntimeException("Error listing git tags: " + String.join("\n", processResult.error()));
        }
        return processResult
                .output()
                .stream()
                .filter(Version::validateFormat)
                .map(Version::parse)
                .reduce((version, version2) -> version.compareTo(version2) >= 0 ? version : version2)
                .orElseThrow();
    }
}
