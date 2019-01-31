package com.akalea.sshtools.domain.system;

public class ProcessInfo {

    private String pid;
    private String command;

    public ProcessInfo(String pid, String command) {
        super();
        this.pid = pid;
        this.command = command;
    }

    @Override
    public String toString() {
        return String.format("ProcessInfo [pid=%s, command=%s]", pid, command);
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
