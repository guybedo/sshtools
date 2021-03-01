package com.akalea.sshtools.service;

import java.util.List;

import com.akalea.sshtools.domain.command.SftpCommand;
import com.akalea.sshtools.domain.command.SshCommand;
import com.akalea.sshtools.domain.command.SshCommandExecution;
import com.akalea.sshtools.domain.connection.SshTunnel;
import com.akalea.sshtools.domain.session.SshSession;
import com.akalea.sshtools.domain.session.SshSessionConfiguration;

public class SshService {

    public static List<SshCommandExecution> ssh(
        SshSessionConfiguration configuration,
        List<SshCommand> commands,
        boolean failOnError,
        boolean sourceProfile) {
        return SshSession
            .of(configuration)
            .sshExec(commands, sourceProfile, failOnError, false);
    }

    public static List<SshCommandExecution> shell(
        SshSessionConfiguration configuration,
        List<SshCommand> commands,
        boolean failOnError) {
        return SshSession
            .of(configuration)
            .sshShell(commands, failOnError, false);
    }

    public static List<SshCommandExecution> sftp(
        SshSessionConfiguration configuration,
        List<SftpCommand> commands,
        boolean failOnError) {
        return SshSession
            .of(configuration)
            .sftp((List<SshCommand>) (List) commands, failOnError, false);
    }

    public static SshTunnel tunnel(
        SshSessionConfiguration configuration,
        String remoteHost,
        int remotePort) {
        return SshSession
            .of(configuration)
            .tunnel(remoteHost, remotePort);
    }
}
