package com.akalea.sshtools.domain.session;

import org.apache.commons.lang3.StringUtils;

public class SshServerInfo {

    private String username;
    private String password;
    private String hostname;
    private int    port = 22;
    private String passkeyFile;
    private String passphrase;

    public SshServerInfo() {

    }

    public SshServerInfo(String username, String hostname, String passkeyFile, String passphrase) {
        super();
        this.username = username;
        this.hostname = hostname;
        this.passkeyFile = passkeyFile;
        this.passphrase = passphrase;
    }

    public SshServerInfo(String username, String password, String hostname) {
        super();
        this.username = username;
        this.password = password;
        this.hostname = hostname;
    }

    public boolean isPasskeyDefined() {
        return !StringUtils.isEmpty(passkeyFile);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPasskeyFile() {
        return passkeyFile;
    }

    public void setPasskeyFile(String passkeyFile) {
        this.passkeyFile = passkeyFile;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
