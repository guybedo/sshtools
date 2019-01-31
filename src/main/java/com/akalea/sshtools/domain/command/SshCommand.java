package com.akalea.sshtools.domain.command;

import java.util.List;
import java.util.function.Function;

public class SshCommand<R> {

    private String command;

    private Function<String, Boolean> resultMatcher;
    private Function<List<String>, R> resultExtractor;

    public SshCommand(String command) {
        super();
        this.command = command;
    }

    public SshCommand(
        String command,
        Function<String, Boolean> resultMatcher,
        Function<List<String>, R> resultExtractor) {
        super();
        this.command = command;
        this.resultMatcher = resultMatcher;
        this.resultExtractor = resultExtractor;
    }

    @Override
    public String toString() {
        return String.format("SshCommand [command=%s]", command);
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Function<String, Boolean> getExpect() {
        return resultMatcher;
    }

    public void setExpect(Function<String, Boolean> expect) {
        this.resultMatcher = expect;
    }

    public Function<List<String>, R> getResultExtractor() {
        return resultExtractor;
    }

    public void setResultExtractor(Function<List<String>, R> extractResult) {
        this.resultExtractor = extractResult;
    }

}
