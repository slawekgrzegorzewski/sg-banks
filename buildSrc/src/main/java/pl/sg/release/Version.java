package pl.sg.release;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Version(int major, int minor, int patch) implements Comparable<Version> {

    public static boolean validateFormat(String version) {
        Pattern p = Pattern.compile("^[0-9]+\\.[0-9]+\\.[0-9]+$");
        Matcher m = p.matcher(version);
        return m.matches();
    }

    public static Version parse(String version) {
        String[] split = version.split("\\.");
        return new Version(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }

    public String toString() {
        return this.major + "." + this.minor + "." + this.patch;
    }

    public int compareTo(@NotNull Version o) {
        int majorDiff = this.major - o.major;
        if (majorDiff != 0) {
            return Integer.signum(majorDiff);
        } else {
            int minorDiff = this.minor - o.minor;
            return minorDiff != 0 ? Integer.signum(minorDiff) : this.patch - o.patch;
        }
    }

    public int major() {
        return this.major;
    }

    public int minor() {
        return this.minor;
    }

    public int patch() {
        return this.patch;
    }
}
