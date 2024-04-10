package com.akalea.sshtools.helper.wrap;

import java.util.List;
import java.util.function.Supplier;

import com.akalea.sshtools.domain.session.SshSession;
import com.akalea.sshtools.domain.system.FileInfo;
import com.akalea.sshtools.helper.File;
import com.akalea.sshtools.helper.impl.FileHelper;

public class FileWrapper implements File {

    private Supplier<SshSession> sessionProvider;
    private boolean              keepSessionAlive = false;

    public FileWrapper(Supplier<SshSession> sessionProvider) {
        super();
        this.sessionProvider = sessionProvider;
    }

    @Override
    public Double getAvailableDiskSpace(String path) {
        return new FileHelper(this.sessionProvider.get().connect(), keepSessionAlive)
            .getAvailableDiskSpace(path);
    }

    @Override
    public Double getAvailableDiskSpacePercentage(String path) {
        return new FileHelper(this.sessionProvider.get().connect(), keepSessionAlive)
            .getAvailableDiskSpacePercentage(path);
    }

    @Override
    public List<FileInfo> listFiles(String path) {
        return new FileHelper(this.sessionProvider.get().connect(), keepSessionAlive)
            .listFiles(path);
    }

    @Override
    public void copyFile(
        String src,
        String dst,
        boolean force,
        boolean recursive) {
        new FileHelper(this.sessionProvider.get().connect(), keepSessionAlive).copyFile(
            src,
            dst,
            force,
            recursive);
    }

    @Override
    public void moveFile(
        String src,
        String dst,
        boolean force) {
        new FileHelper(this.sessionProvider.get().connect(), keepSessionAlive).moveFile(src, dst, force);
    }

    @Override
    public void deleteFile(
        String path,
        boolean force,
        boolean recursive) {
        new FileHelper(this.sessionProvider.get().connect(), keepSessionAlive).deleteFile(
            path,
            force,
            recursive);
    }
}