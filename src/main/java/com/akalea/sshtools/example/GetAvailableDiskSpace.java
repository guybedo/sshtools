package com.akalea.sshtools.example;

import com.akalea.sshtools.domain.session.SshServerInfo;
import com.akalea.sshtools.domain.session.SshSessionConfiguration;
import com.akalea.sshtools.process.SshProcesses;

public class GetAvailableDiskSpace {

    public static void main(String[] args) {
        SshServerInfo serverInfo =
            new SshServerInfo(
                "login",
                "localhost",
                "/home/user/.ssh/id_rsa",
                null);

        String path = "/";
        Double result =
            SshProcesses
                .files()
                .getAvailableDiskSpace(
                    new SshSessionConfiguration().setServer(serverInfo),
                    path);
        System.out.println(String.format("Available %f", result));

    }
}
