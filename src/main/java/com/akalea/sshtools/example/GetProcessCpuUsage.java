package com.akalea.sshtools.example;

import com.akalea.sshtools.domain.session.SshServerInfo;
import com.akalea.sshtools.domain.session.SshSessionConfiguration;
import com.akalea.sshtools.process.SshProcesses;

public class GetProcessCpuUsage {

    public static void main(String[] args) {
        SshServerInfo serverInfo =
            new SshServerInfo(
                "login",
                "localhost",
                "/home/user/.ssh/id_rsa",
                null);

        String processName = "mediation";
        Double result =
            SshProcesses
                .processes()
                .getProcessCpuUsage(
                    new SshSessionConfiguration().setServer(serverInfo),
                    processName);
        System.out.println(String.format("Cpu usage %f", result));

    }
}
