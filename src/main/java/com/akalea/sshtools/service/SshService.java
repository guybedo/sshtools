package com.akalea.sshtools.service;

import java.util.List;

import com.akalea.sshtools.domain.command.SftpCommand;
import com.akalea.sshtools.domain.command.SshCommand;
import com.akalea.sshtools.domain.command.SshCommandExecution;
import com.akalea.sshtools.domain.connection.SshTunnel;
import com.akalea.sshtools.domain.session.SshServerInfo;
import com.akalea.sshtools.domain.session.SshSession;

public class SshService {

    public static List<SshCommandExecution> ssh(
        SshServerInfo server,
        List<SshCommand> commands,
        boolean failOnError,
        boolean sourceProfile) {
        return SshSession
            .of(server)
            .sshExec(commands, sourceProfile, failOnError, false);
    }

    public static List<SshCommandExecution> shell(
        SshServerInfo server,
        List<SshCommand> commands,
        boolean failOnError) {
        return SshSession
            .of(server)
            .sshShell(commands, failOnError, false);
    }

    public static List<SshCommandExecution> sftp(
        SshServerInfo server,
        List<SftpCommand> commands,
        boolean failOnError) {
        return SshSession
            .of(server)
            .sftp((List<SshCommand>) (List) commands, failOnError, false);
    }

    public static SshTunnel tunnel(
        SshServerInfo server,
        String remoteHost,
        int remotePort) {
        return SshSession
            .of(server)
            .tunnel(remoteHost, remotePort);
    }
}
