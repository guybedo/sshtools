package com.akalea.sshtools.examples.sftp;

import java.util.List;

import com.akalea.sshtools.Ssh;
import com.akalea.sshtools.domain.command.SftpCommand;
import com.akalea.sshtools.domain.session.SshServerInfo;
import com.google.common.collect.Lists;

public class ExecuteSftpCommands {

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

        String localFile = "/tmp/test.txt";
        String remoteFile = String.format("/home/%s/test.txt", username);
        String downloadedFile = "/tmp/test.copy.txt";
        List<SftpCommand> commands =
            Lists.newArrayList(
                SftpCommand.put(localFile, remoteFile),
                SftpCommand.get(remoteFile, downloadedFile));
        Ssh
            .of(serverInfo)
            .sftp()
            .execute(commands)
            .stream()
            .forEach(execution -> {
                System.out.println(execution);
            });
        ;

    }
}
