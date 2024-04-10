package com.akalea.sshtools.domain.helpers;

import java.util.List;

import com.akalea.sshtools.domain.system.FileInfo;

public interface File {

    Double getAvailableDiskSpace(String path);

    Double getAvailableDiskSpacePercentage(
        String path);

    List<FileInfo> listFiles(String path);

    default void copyFile(String src, String dst) {
        copyFile(src, dst, false, false);
    }

    void copyFile(
        String src,
        String dst,
        boolean force,
        boolean recursive);

    default void moveFile(String src, String dst) {
        moveFile(src, dst, false);
    }

    void moveFile(
        String src,
        String dst,
        boolean force);

    default void deleteFile(String path) {
        deleteFile(path, false, false);
    }

    void deleteFile(
        String path,
        boolean force,
        boolean recursive);

}