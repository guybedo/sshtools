package com.akalea.sshtools.example.ssh;

import java.util.List;

import com.akalea.sshtools.Ssh;
import com.akalea.sshtools.domain.session.SshServerInfo;
import com.akalea.sshtools.domain.system.FileInfo;

public class ListFiles {

    public static void main(String[] args) {
        String username = "";
        String host = "";
        String privateKeyFilePath = "";
        SshServerInfo serverInfo =
            new SshServerInfo(
                username,
                host,
                privateKeyFilePath,
                null);

        List<FileInfo> files =
                Ssh
                .of(serverInfo)
                .file()
                .listFiles("/home/user");
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
