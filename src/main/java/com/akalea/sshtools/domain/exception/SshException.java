package com.akalea.sshtools.domain.exception;

import com.jcraft.jsch.JSchException;

public class SshException extends RuntimeException {

    public SshException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public SshException(
        String message,
        Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public SshException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public SshException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public SshException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    public static SshException fromJSchException(JSchException e) {
        if (e.getMessage() == null)
            return new SshException();
        if (e.getMessage().contains("invalid privatekey"))
            return new SshPrivateKeyException();
        if (e.getMessage().contains("invalid publickey"))
            return new SshPublicKeyException();
        return new SshException(e.getMessage());
    }

    public static SshException invalidPrivateKey() {
        return new SshException("Invalid Private Key");
    }

    public static SshException invalidPublicKey() {
        return new SshException("Invalid Private Key");
    }
}
