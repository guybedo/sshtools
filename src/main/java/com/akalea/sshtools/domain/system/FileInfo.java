package com.akalea.sshtools.domain.system;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public class FileInfo {

    private String name;
    private String path;

    private String type;

    private String mode;

    private String owner;
    private String group;

    private long size;

    private LocalDateTime lastUpdateDate;
    private LocalDateTime lastAccessDate;

    @Override
    public String toString() {
        return String.format("FileInfo [name=%s, path=%s, size=%s]", name, path, size);
    }

    public static FileInfo fromStdout(String path, String line) {
        List<String> tokens =
            Lists
                .newArrayList(line.split(" "))
                .stream()
                .filter(t -> !StringUtils.isEmpty(t))
                .collect(Collectors.toList());
        if (tokens.size() < 9)
            return null;
        return new FileInfo()
            .setPath(path)
            .setName(tokens.get(tokens.size() - 1))
            .setMode(tokens.get(0))
            .setType(tokens.get(1))
            .setOwner(tokens.get(2))
            .setGroup(tokens.get(3))
            .setSize(Long.parseLong(tokens.get(4)));
    }

    public String getName() {
        return name;
    }

    public FileInfo setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public FileInfo setType(String type) {
        this.type = type;
        return this;
    }

    public String getMode() {
        return mode;
    }

    public FileInfo setMode(String mode) {
        this.mode = mode;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public FileInfo setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public FileInfo setGroup(String group) {
        this.group = group;
        return this;
    }

    public long getSize() {
        return size;
    }

    public FileInfo setSize(long size) {
        this.size = size;
        return this;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public FileInfo setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
        return this;
    }

    public LocalDateTime getLastAccessDate() {
        return lastAccessDate;
    }

    public FileInfo setLastAccessDate(LocalDateTime lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
        return this;
    }

    public String getPath() {
        return path;
    }

    public FileInfo setPath(String path) {
        this.path = path;
        return this;
    }

}
