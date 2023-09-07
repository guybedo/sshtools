package com.akalea.sshtools.domain.session;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class SshServerInfo implements Serializable {

    private String host;
    private int    port = 22;

    private String username;
    private String password;

    private SshKey key;

    public SshServerInfo() {

    }

    public SshServerInfo(
        String username,
        String hostname) {
        super();
        this.username = username;
        this.host = hostname;
    }

    public SshServerInfo(
        String username,
        String hostname,
        String privateKeyFile,
        String passphrase) {
        super();
        this.username = username;
        this.host = hostname;
        this.key =
            new SshKey()
                .setPrivateKeyFile(privateKeyFile)
                .setPassphrase(passphrase);
    }

    public SshServerInfo(String username, String password, String hostname) {
        super();
        this.username = username;
        this.password = password;
        this.host = hostname;
    }

    public boolean isPasskey() {
        return this.key != null
            &&
            (!StringUtils.isEmpty(this.key.getPrivateKeyFile())
                || !StringUtils.isEmpty(this.key.getPrivateKey()));
    }

    public boolean isPrivateKeyFileDefined() {
        return this.key != null && !StringUtils.isEmpty(this.key.getPrivateKeyFile());
    }

    public boolean isPublicKeyFileDefined() {
        return this.key != null && !StringUtils.isEmpty(this.key.getPublicKeyFile());
    }

    public boolean isUserPasswordAuth() {
        return this.key != null
            && StringUtils.stripToNull(this.username) != null
            && StringUtils.stripToNull(this.password) != null;
    }

    @Override
    public String toString() {
        return "SshServerInfo [host=" + host + ", port=" + port + ", username=" + username + "]";
    }

    public SshServerInfo setPrivateKey(String privateKey) {
        if (this.key == null)
            this.key = new SshKey();
        key.setPrivateKey(privateKey);
        return this;
    }

    public SshServerInfo setPrivateKeyFile(String privateKeyFile) {
        if (this.key == null)
            this.key = new SshKey();
        key.setPrivateKeyFile(privateKeyFile);
        return this;
    }

    public SshServerInfo setPublicKey(String publicKey) {
        if (this.key == null)
            this.key = new SshKey();
        key.setPublicKey(publicKey);
        return this;
    }

    public SshServerInfo setPublicKeyFile(String publicKeyFile) {
        if (this.key == null)
            this.key = new SshKey();
        key.setPublicKeyFile(publicKeyFile);
        return this;
    }

    public SshServerInfo setPassphrase(String passphrase) {
        if (this.key == null)
            this.key = new SshKey();
        key.setPassphrase(passphrase);
        return this;
    }

    public String getUsername() {
        return username;
    }

    public SshServerInfo setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getHost() {
        return host;
    }

    public SshServerInfo setHost(String hostname) {
        this.host = hostname;
        return this;
    }

    public int getPort() {
        return port;
    }

    public SshServerInfo setPort(int port) {
        this.port = port;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public SshServerInfo setPassword(String password) {
        this.password = password;
        return this;
    }

    public SshKey getKey() {
        return key;
    }

    public SshServerInfo setKey(SshKey key) {
        this.key = key;
        return this;
    }

}
