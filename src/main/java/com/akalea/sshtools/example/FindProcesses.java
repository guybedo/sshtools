package com.akalea.sshtools.example;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.akalea.sshtools.domain.session.SshServerInfo;
import com.akalea.sshtools.domain.session.SshSessionConfiguration;
import com.akalea.sshtools.domain.system.ProcessInfo;
import com.akalea.sshtools.helper.CommandHelper;
import com.akalea.sshtools.process.SshProcesses;
import com.akalea.sshtools.service.SshService;
import com.google.common.collect.Lists;

public class FindProcesses {

    public static void main(String[] args) {
        SshServerInfo serverInfo =
            new SshServerInfo(
                "login",
                "localhost",
                "/home/user/.ssh/id_rsa",
                null);

        String processName = "java";
        List<ProcessInfo> processes =
            SshProcesses
                .processes()
                .findProcessesByName(
                    new SshSessionConfiguration().setServer(serverInfo),
                    processName);
        System.out.println(String.format("Found %d processes", processes.size()));

    }
}
