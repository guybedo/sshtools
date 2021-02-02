package com.akalea.sshtools.example;

import java.util.List;

import com.akalea.sshtools.domain.session.SshServerInfo;
import com.akalea.sshtools.domain.system.ProcessInfo;
import com.akalea.sshtools.process.SshProcesses;

public class KillProcesses {

    public static void main(String[] args) {
        SshServerInfo serverInfo =
            new SshServerInfo(
                "login",
                "localhost",
                "/home/user/.ssh/id_rsa",
                null);

        String processName = "java";
        List<ProcessInfo> processes =
            SshProcesses.processes().findProcessesByName(serverInfo, processName);
        System.out.println(String.format("Found %d processes", processes.size()));

        System.out.println("Killing them all");
        SshProcesses.processes().killProcessesByName(serverInfo, processName, 30, 1);

        processes = SshProcesses.processes().findProcessesByName(serverInfo, processName);
        System.out.println(String.format("Remains %d processes", processes.size()));

    }
}
