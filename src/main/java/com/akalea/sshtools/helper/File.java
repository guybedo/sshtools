package com.akalea.sshtools.helper;

import java.util.List;

import com.akalea.sshtools.domain.system.FileInfo;

public interface File {

    Double getAvailableDiskSpace(String path);

    Double getAvailableDiskSpacePercentage(
        String path);

    List<FileInfo> listFiles(String path);

    void copyFile(
        String src,
        String dst,
        boolean force,
        boolean recursive);

    void moveFile(
        String src,
        String dst,
        boolean force);

    void deleteFile(
        String path,
        boolean force,
        boolean recursive);

}