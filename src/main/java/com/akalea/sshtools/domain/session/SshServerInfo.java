package com.akalea.sshtools.domain.session;

import org.apache.commons.lang3.StringUtils;

public class SshServerInfo {

    private String username;
    private String password;
    private String hostname;
    private int    port = 22;

    private String privateKey;
    private String publicKey;

    private String privateKeyFile;
    private String passphrase;

    public SshServerInfo() {

    }
    
    public SshServerInfo(
        String username,
        String hostname) {
        super();
        this.username = username;
        this.hostname = hostname;
    }

    public SshServerInfo(
        String username,
        String hostname,
        String privateKeyFile,
        String passphrase) {
        super();
        this.username = username;
        this.hostname = hostname;
        this.privateKeyFile = privateKeyFile;
        this.passphrase = passphrase;
    }

    public SshServerInfo(String username, String password, String hostname) {
        super();
        this.username = username;
        this.password = password;
        this.hostname = hostname;
    }

    public boolean isPasskeyDefined() {
        return !StringUtils.isEmpty(privateKeyFile) || !StringUtils.isEmpty(privateKey);
    }

    public String getUsername() {
        return username;
    }

    public SshServerInfo setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getHostname() {
        return hostname;
    }

    public SshServerInfo setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public int getPort() {
        return port;
    }

    public SshServerInfo setPort(int port) {
        this.port = port;
        return this;
    }

    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    public SshServerInfo setPrivateKeyFile(String passkeyFile) {
        this.privateKeyFile = passkeyFile;
        return this;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public SshServerInfo setPassphrase(String passphrase) {
        this.passphrase = passphrase;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public SshServerInfo setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public SshServerInfo setPrivateKey(String passkey) {
        this.privateKey = passkey;
        return this;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public SshServerInfo setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

}
