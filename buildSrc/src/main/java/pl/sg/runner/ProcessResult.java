package pl.sg.runner;

import java.util.List;

public record ProcessResult(List<String> output, List<String> error, Integer exitCode) {
}
