package com.akalea.sshtools.domain.exception;

public class SshPrivateKeyException extends SshException {

    public SshPrivateKeyException() {
        super("Invalid Private Key");
    }

}
