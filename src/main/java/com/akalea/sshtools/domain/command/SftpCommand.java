package com.akalea.sshtools.domain.command;

import org.apache.commons.lang3.StringUtils;

public class SftpCommand extends SshCommand<Boolean> {

    public static enum SftpCommandId {
            rm, rename, ls, get, put, quit, chgrp, chown, chmod, rmdir, mkdir, pwd
    }

    private SftpCommandId id;
    private Object[]      args;

    public SftpCommand(SftpCommandId type, Object... args) {
        super(toSshCommand(type, args));
        this.id = type;
        this.args = args;
    }

    public static SftpCommand rm(Object... args) {
        return new SftpCommand(SftpCommandId.rm, args);
    }

    public static SftpCommand rename(Object... args) {
        return new SftpCommand(SftpCommandId.rename, args);
    }

    public static SftpCommand ls(Object... args) {
        return new SftpCommand(SftpCommandId.ls, args);
    }

    public static SftpCommand get(Object... args) {
        return new SftpCommand(SftpCommandId.get, args);
    }

    public static SftpCommand put(Object... args) {
        return new SftpCommand(SftpCommandId.put, args);
    }

    public static SftpCommand quit(Object... args) {
        return new SftpCommand(SftpCommandId.quit, args);
    }

    public static SftpCommand chgrp(Object... args) {
        return new SftpCommand(SftpCommandId.chgrp, args);
    }

    public static SftpCommand chown(Object... args) {
        return new SftpCommand(SftpCommandId.chown, args);
    }

    public static SftpCommand chmod(Object... args) {
        return new SftpCommand(SftpCommandId.chmod, args);
    }

    public static SftpCommand rmdir(Object... args) {
        return new SftpCommand(SftpCommandId.rmdir, args);
    }

    public static SftpCommand mkdir(Object... args) {
        return new SftpCommand(SftpCommandId.mkdir, args);
    }

    public static SftpCommand pwd(Object... args) {
        return new SftpCommand(SftpCommandId.pwd, args);
    }

    private static String toSshCommand(SftpCommandId type, Object... args) {
        return String.format("%s %s", type.toString(), StringUtils.join(args));
    }

    @Override
    public String toString() {
        return String.format(
            "SftpCommand [command=%s]",
            toSshCommand(id, args));
    }

    public SftpCommandId getId() {
        return id;
    }

    public void setId(SftpCommandId type) {
        this.id = type;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

}
