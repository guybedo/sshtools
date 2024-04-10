package com.akalea.sshtools.examples.ssh;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.akalea.sshtools.Ssh;
import com.akalea.sshtools.domain.command.SshCommand;
import com.akalea.sshtools.domain.session.SshServerInfo;
import com.google.common.collect.Lists;

public class ExecuteSshCommands {

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
        List<SshCommand> commands =
            Lists.newArrayList(
                new SshCommand("java -version"),
                new SshCommand("cd /data"),
                new SshCommand("ls -l"));
        Ssh
            .of(serverInfo)
            .command()
            .execute(commands)
            .stream()
            .forEach(execution -> {
                System.out.println(StringUtils.join(execution.getStdout(), "\n"));
                if (execution.getStderr() != null && !execution.getStderr().isEmpty())
                    System.out.println(StringUtils.join(execution.getStderr(), "\n"));
            });
        ;

    }
}
