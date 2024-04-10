package com.akalea.sshtools.examples.ssh;

import com.akalea.sshtools.Ssh;
import com.akalea.sshtools.domain.session.SshServerInfo;

public class GetProcessCpuUsage {

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

        String processName = "mediation";
        Double result =
            Ssh
                .of(serverInfo)
                .process()
                .getProcessCpuUsage(processName);
        System.out.println(String.format("Cpu usage %f", result));

    }
}
