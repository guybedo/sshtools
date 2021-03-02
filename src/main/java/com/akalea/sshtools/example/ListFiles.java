package com.akalea.sshtools.example;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.akalea.sshtools.domain.session.SshServerInfo;
import com.akalea.sshtools.domain.session.SshSessionConfiguration;
import com.akalea.sshtools.domain.system.FileInfo;
import com.akalea.sshtools.helper.CommandHelper;
import com.akalea.sshtools.process.SshProcesses;
import com.akalea.sshtools.service.SshService;
import com.google.common.collect.Lists;

public class ListFiles {

    public static void main(String[] args) {
        SshServerInfo serverInfo =
            new SshServerInfo(
                "login",
                "localhost",
                "/home/user/.ssh/id_rsa",
                null);

        List<FileInfo> files =
            SshProcesses
                .files()
                .listFiles(new SshSessionConfiguration().setServer(serverInfo), "/home/user");
        System.out.println(
            String.format(
                "Found %d files %s",
                files.size(),
                files
                    .stream()
                    .map(p -> p.toString())
                    .reduce((a, b) -> String.format("%s,%s", a, b))
                    .get()));

    }
}
