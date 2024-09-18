package com.akalea.sshtools.domain.command;

import java.util.List;

public class SshCommandExecution<R> {

    private SshCommand<R> command;

    private List<String> stdout;
    private List<String> stderr;

    private R result;

    private Throwable t;

    public SshCommandExecution(SshCommand<R> command) {
        super();
        this.command = command;
    }

    public SshCommandExecution setResult(List<String> stdout, List<String> stderr) {
        setResult(stdout, null, null);
        return this;
    }

    public SshCommandExecution setResult(List<String> stdout, List<String> stderr, R result) {
        this.stdout = stdout;
        this.stderr = stderr;
        this.result = result;
        return this;
    }

    @Override
    public String toString() {
        String str =
            String.format(
                "%s (%s)",
                this.command.toString(),
                this.isError() ? "ko" : "ok");
        if (isError() && this.t != null)
            str = String.format("%s: %s", str, this.t.getMessage());
        else if (this.result != null)
            str = String.format("%s: %s", str, this.result.toString());
        return str;
    }

    public boolean isError() {
        return t != null || (stderr != null && stderr.size() > 0);
    }

    public SshCommand<R> getCommand() {
        return command;
    }

    public List<String> getStdout() {
        return stdout;
    }

    public void setStdout(List<String> stdout) {
        this.stdout = stdout;
    }

    public List<String> getStderr() {
        return stderr;
    }

    public void setStderr(List<String> stderr) {
        this.stderr = stderr;
    }

    public R getResult() {
        return result;
    }

    public void setResult(R result) {
        this.result = result;
    }

    public void setCommand(SshCommand<R> command) {
        this.command = command;
    }

    public Throwable getT() {
        return t;
    }

    public void setT(Throwable t) {
        this.t = t;
    }

}
