package com.akalea.sshtools.example.ssh;

import java.util.List;

import com.akalea.sshtools.Ssh;
import com.akalea.sshtools.domain.session.SshServerInfo;
import com.akalea.sshtools.domain.system.ProcessInfo;

public class FindProcesses {

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

        String processName = "java";
        List<ProcessInfo> processes =
            Ssh
                .of(serverInfo)
                .process()
                .findProcessesByName(processName);
        System.out.println(String.format("Found %d processes", processes.size()));

    }
}
