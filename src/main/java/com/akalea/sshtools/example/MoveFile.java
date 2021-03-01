package com.akalea.sshtools.example;

import com.akalea.sshtools.domain.session.SshServerInfo;
import com.akalea.sshtools.domain.session.SshSessionConfiguration;
import com.akalea.sshtools.process.SshProcesses;

public class MoveFile {

    public static void main(String[] args) {
        SshServerInfo serverInfo =
            new SshServerInfo(
                "login",
                "localhost",
                "/home/user/.ssh/id_rsa",
                null);
        SshProcesses
            .files()
            .moveFile(
                new SshSessionConfiguration().setServer(serverInfo),
                "/home/user/test.txt",
                "/home/user/test2.txt",
                true);

    }
}
