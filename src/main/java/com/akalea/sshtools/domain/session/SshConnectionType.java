package com.akalea.sshtools.domain.session;

public enum SshConnectionType {
        command("exec"), shell("shell"), sftp("sftp");

    private String channelType;

    private SshConnectionType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannelType() {
        return channelType;
    }

}