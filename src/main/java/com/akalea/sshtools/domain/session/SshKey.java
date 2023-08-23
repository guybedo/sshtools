package com.akalea.sshtools.domain.session;

public class SshKey {

    protected String privateKey;
    protected String privateKeyFile;

    protected String publicKey;
    protected String publicKeyFile;
    protected String passphrase;

    public String getPrivateKey() {
        return privateKey;
    }

    public SshKey setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    public SshKey setPrivateKeyFile(String privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
        return this;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public SshKey setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public String getPublicKeyFile() {
        return publicKeyFile;
    }

    public SshKey setPublicKeyFile(String publicKeyFile) {
        this.publicKeyFile = publicKeyFile;
        return this;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public SshKey setPassphrase(String passphrase) {
        this.passphrase = passphrase;
        return this;
    }

}
