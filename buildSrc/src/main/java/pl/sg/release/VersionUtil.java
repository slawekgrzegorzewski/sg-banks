package pl.sg.release;

import org.jetbrains.annotations.NotNull;
import pl.sg.runner.CommandRunner;
import pl.sg.runner.ProcessResult;

import java.util.List;

public class VersionUtil {

    private VersionUtil() {
    }

    public static String getNextMajorVersion() {
        Version version = getCurrentVersion();
        return new Version(version.major() + 1, 0, 0).toString();
    }

    public static String getNextMinorVersion() {
        Version version = getCurrentVersion();
        return new Version(version.major(), version.minor() + 1, 0).toString();
    }

    public static String getNextPatchVersion() {
        Version version = getCurrentVersion();
        return new Version(version.major(), version.minor(), version.patch() + 1).toString();
    }

    public static @NotNull Version getCurrentVersion() {
        ProcessResult processResult = CommandRunner.runCommand(List.of("git", "tag", "-l"));
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
