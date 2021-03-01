package com.akalea.sshtools.example;

import java.util.List;
import java.util.UUID;

import com.akalea.sshtools.domain.command.SftpCommand;
import com.akalea.sshtools.domain.command.SftpCommand.SftpCommandType;
import com.akalea.sshtools.domain.session.SshServerInfo;
import com.akalea.sshtools.domain.session.SshSessionConfiguration;
import com.akalea.sshtools.service.SshService;
import com.google.common.collect.Lists;

public class ExecuteSftpCommands {

    public static void main(String[] args) {
        SshServerInfo serverInfo =
            new SshServerInfo(
                "login",
                "localhost",
                "/home/user/.ssh/id_rsa",
                null);

        String localFile = "/tmp/test.txt";
        String remoteFile = "/home/user/test.txt";
        String localCopy = "/tmp/test.copy.txt";
        List<SftpCommand> commands =
            Lists.newArrayList(
                new SftpCommand(SftpCommandType.put, localFile, remoteFile),
                new SftpCommand(SftpCommandType.get, remoteFile, localCopy));
        SshService
            .sftp(new SshSessionConfiguration().setServer(serverInfo), commands, false)
            .stream()
            .forEach(execution -> {
                System.out.println(
                    String.format(
                        "%s: %s",
                        execution.getCommand().toString(),
                        String.valueOf(!execution.isError())));
            });
        ;

    }
}
