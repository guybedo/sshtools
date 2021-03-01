package com.akalea.sshtools.example;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.akalea.sshtools.domain.session.SshServerInfo;
import com.akalea.sshtools.domain.session.SshSessionConfiguration;
import com.akalea.sshtools.domain.system.ProcessInfo;
import com.akalea.sshtools.helper.CommandHelper;
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
        SshService
            .ssh(
                new SshSessionConfiguration().setServer(serverInfo),
                Lists.newArrayList(CommandHelper.processes().findProcesses(processName)),
                false,
                false)
            .stream()
            .forEach(execution -> {
                if (execution.getStderr() != null && !execution.getStderr().isEmpty())
                    System.out.println(StringUtils.join(execution.getStderr(), "\n"));
                else {
                    List<ProcessInfo> processes = ((List<ProcessInfo>) execution.getResult());
                    System.out.println(
                        String.format(
                            "Found %d processes %s",
                            processes.size(),
                            processes
                                .stream()
                                .map(p -> p.toString())
                                .reduce((a, b) -> String.format("%s,%s", a, b))
                                .get()));
                }

            });
        ;

    }
}
