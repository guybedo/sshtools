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

    public static SftpCommand rm(String path) {
        return new SftpCommand(SftpCommandId.rm, path);
    }

    public static SftpCommand rename(String oldpath, String newpath) {
        return new SftpCommand(SftpCommandId.rename, oldpath, newpath);
    }

    public static SftpCommand ls(String path) {
        return new SftpCommand(SftpCommandId.ls, path);
    }

    public static SftpCommand get(String src, String dst) {
        return new SftpCommand(SftpCommandId.get, src, dst);
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

    public static SftpCommand chown(int uid, String path) {
        return new SftpCommand(SftpCommandId.chown, uid, path);
    }

    public static SftpCommand chmod(int permissions, String path) {
        return new SftpCommand(SftpCommandId.chmod, permissions, path);
    }

    public static SftpCommand rmdir(String path) {
        return new SftpCommand(SftpCommandId.rmdir, path);
    }

    public static SftpCommand mkdir(String path) {
        return new SftpCommand(SftpCommandId.mkdir, path);
    }

    public static SftpCommand pwd() {
        return new SftpCommand(SftpCommandId.pwd);
    }

    private static String toSshCommand(SftpCommandId type, Object... args) {
        return String.format("%s %s", type.toString(), StringUtils.join(args));
    }

    @Override
    public String toString() {
        return String.format(
            "[%s]",
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
