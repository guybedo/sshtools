package com.akalea.sshtools.domain.exception;

public class SshPublicKeyException extends SshException {

    public SshPublicKeyException() {
        super("Invalid Public Key");
    }

}
