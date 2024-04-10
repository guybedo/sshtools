package com.akalea.sshtools.domain.helpers.impl;

import java.util.List;

import com.akalea.sshtools.domain.helpers.File;
import com.akalea.sshtools.domain.session.SshSession;
import com.akalea.sshtools.domain.system.FileInfo;
import com.google.common.collect.Lists;

public class FileHelper implements File {

    private SshSession session;
    private boolean    keepSessionAlive;

    public FileHelper(SshSession session, boolean keepSessionAlive) {
        super();
        this.session = session;
        this.keepSessionAlive = keepSessionAlive;
    }

    @Override
    public Double getAvailableDiskSpace(String path) {
        return session
            .sshExec(
                Lists.newArrayList(
                    SshCommands
                        .files()
                        .getAvailableDiskSpace(path)),
                false,
                false,
                this.keepSessionAlive)
            .stream()
            .findFirst()
            .map(e -> (Double) e.getResult())
            .orElse(null);
    }

    @Override
    public Double getAvailableDiskSpacePercentage(
        String path) {
        return session
            .sshExec(
                Lists.newArrayList(
                    SshCommands
                        .files()
                        .getAvailableDiskSpacePercentage(path)),
                this.keepSessionAlive)
            .stream()
            .findFirst()
            .map(e -> (Double) e.getResult())
            .orElse(null);
    }

    @Override
    public List<FileInfo> listFiles(String path) {
        return session
            .sshExec(
                Lists.newArrayList(
                    SshCommands
                        .files()
                        .listFiles(path)),
                this.keepSessionAlive)
            .stream()
            .findFirst()
            .map(e -> (List<FileInfo>) e.getResult())
            .orElse(Lists.newArrayList());
    }

    @Override
    public void copyFile(
        String src,
        String dst,
        boolean force,
        boolean recursive) {
        session
            .sshExec(
                Lists.newArrayList(
                    SshCommands
                        .files()
                        .copyFile(src, dst, force, recursive)),
                this.keepSessionAlive);
    }

    @Override
    public void moveFile(
        String src,
        String dst,
        boolean force) {
        session
            .sshExec(
                Lists.newArrayList(
                    SshCommands
                        .files()
                        .moveFile(src, dst, force)),
                this.keepSessionAlive);
    }

    @Override
    public void deleteFile(
        String path,
        boolean force,
        boolean recursive) {
        session
            .sshExec(
                Lists.newArrayList(
                    SshCommands
                        .files()
                        .deleteFile(path, force, recursive)),
                this.keepSessionAlive);
    }
}