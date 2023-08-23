package com.akalea.sshtools.example;

import com.akalea.sshtools.domain.session.SshServerInfo;
import com.akalea.sshtools.domain.session.SshSessionConfiguration;
import com.akalea.sshtools.process.SshProcesses;

public class GetCpuUsage {

    public static void main(String[] args) {
        SshServerInfo serverInfo =
            new SshServerInfo(
                "login",
                "localhost",
                "/home/user/.ssh/id_rsa",
                null);

        Float result =
            SshProcesses
                .cpuInfo()
                .getCpuUsage(new SshSessionConfiguration().setServer(serverInfo));
        System.out.println(String.format("Cpu usage %f", result));

    }
}
