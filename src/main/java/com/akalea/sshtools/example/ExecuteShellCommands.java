package com.akalea.sshtools.example;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.akalea.sshtools.domain.command.SshCommand;
import com.akalea.sshtools.domain.session.SshServerInfo;
import com.akalea.sshtools.service.SshService;
import com.google.common.collect.Lists;

public class ExecuteShellCommands {

    public static void main(String[] args) {
        SshServerInfo serverInfo =
            new SshServerInfo(
                "login",
                "localhost",
                "/home/user/.ssh/id_rsa",
                null);
        List<SshCommand> commands =
            Lists.newArrayList(
                new SshCommand("java -version"),
                new SshCommand("cd /data"),
                new SshCommand("ls -l"));
        SshService
            .shell(serverInfo, commands, false)
            .stream()
            .forEach(execution -> {
                System.out.println(StringUtils.join(execution.getStdout(), "\n"));
                if (execution.getStderr() != null && !execution.getStderr().isEmpty())
                    System.out.println(StringUtils.join(execution.getStderr(), "\n"));
            });
        ;

    }
}
