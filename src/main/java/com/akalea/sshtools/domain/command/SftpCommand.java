package com.akalea.sshtools.domain.command;

import org.apache.commons.lang3.StringUtils;

public class SftpCommand extends SshCommand<Boolean> {

    public static enum SftpCommandType {
            rm, rename, ls, get, put, quit, chgrp, chown, chmod, rmdir, mkdir, pwd
    }

    private SftpCommandType type;
    private Object[]        args;

    public SftpCommand(SftpCommandType type, Object... args) {
        super(toSshCommand(type, args));
        this.type = type;
        this.args = args;
    }

    private static String toSshCommand(SftpCommandType type, Object... args) {
        return String.format("%s %s", type.toString(), StringUtils.join(args));
    }

    @Override
    public String toString() {
        return String.format(
            "SftpCommand [command=%s]",
            toSshCommand(type, args));
    }

    public SftpCommandType getType() {
        return type;
    }

    public void setType(SftpCommandType type) {
        this.type = type;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

}
