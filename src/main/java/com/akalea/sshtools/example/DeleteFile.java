package com.akalea.sshtools.example;

import com.akalea.sshtools.domain.session.SshServerInfo;
import com.akalea.sshtools.domain.session.SshSessionConfiguration;
import com.akalea.sshtools.process.SshProcesses;

public class DeleteFile {

    public static void main(String[] args) {
        SshServerInfo serverInfo =
            new SshServerInfo(
                "login",
                "localhost",
                "/home/user/.ssh/id_rsa",
                null);
        SshProcesses
            .files()
            .deleteFile(
                new SshSessionConfiguration().setServer(serverInfo),
                "/home/user/test.txt",
                true,
                true);

    }
}
