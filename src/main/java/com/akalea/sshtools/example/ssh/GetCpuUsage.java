package com.akalea.sshtools.example.ssh;

import com.akalea.sshtools.Ssh;
import com.akalea.sshtools.domain.session.SshServerInfo;

public class GetCpuUsage {

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

        Float result =
            Ssh
                .of(serverInfo)
                .cpuInfo()
                .getCpuUsage();
        System.out.println(String.format("Cpu usage %f", result));

    }
}
