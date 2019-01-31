package com.akalea.sshtools.domain.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.akalea.sshtools.domain.command.SftpCommand;
import com.akalea.sshtools.domain.command.SshCommand;
import com.akalea.sshtools.domain.command.SshCommandExecution;
import com.akalea.sshtools.domain.connection.SftpConnection;
import com.akalea.sshtools.domain.connection.SshConnection;
import com.akalea.sshtools.domain.connection.SshExecConnection;
import com.akalea.sshtools.domain.connection.SshShellConnection;
import com.google.common.collect.Lists;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

@SuppressWarnings({ "rawtypes", "unused" })
public class SshSession {

    private Session session;

    private SshSession(Session session) {
        this(session, false);
    }

    private SshSession(Session session, boolean strictHostKeyChecking) {
        super();
        this.session = session;
        strictHostKeyChecking(strictHostKeyChecking);
    }

    public static SshSession of(SshServerInfo server) {
        return ssh(server);
    }

    public static SshSession ssh(SshServerInfo server) {
        SshSession session =
            server.isPasskeyDefined()
                ? sshSession(
                    server.getUsername(),
                    server.getHostname(),
                    server.getPort(),
                    server.getPasskeyFile(),
                    server.getPassphrase())
                : sshSession(
                    server.getUsername(),
                    server.getHostname(),
                    server.getPort(),
                    server.getPassword());
        return session;
    }

    private static SshSession sshSession(
        String username,
        String hostname,
        int port,
        String passkeyFile,
        String passphrase) {
        try {
            JSch jsch = new JSch();
            jsch.addIdentity(passkeyFile, passphrase);
            Session session = jsch.getSession(username, hostname, port);
            return new SshSession(session);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static SshSession sshSession(
        String username,
        String hostname,
        int port,
        String password) {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, hostname, port);
            session.setPassword(password);
            return new SshSession(session);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SshSession strictHostKeyChecking(boolean enabled) {
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", enabled ? "yes" : "no");
        session.setConfig(config);
        return this;
    }

    public List<SshCommandExecution> sshExec(
        List<SshCommand> commands,
        boolean sourceProfiles,
        boolean failOnError,
        boolean keepAlive) {
        try {
            assertConnected();
            SshConnection connection = SshConnection.of(SshConnectionType.command).apply(session);
            if (sourceProfiles)
                commands = withSourceProfiles(commands);
            List<SshCommandExecution> executions =
                connection.executeCommands(commands, failOnError);
            return executions;
        } finally {
            if (!keepAlive)
                disconnect();
        }
    }

    public List<SshCommandExecution> sshShell(
        List<SshCommand> commands,
        boolean failOnError,
        boolean keepAlive) {
        try {
            assertConnected();
            SshConnection connection = SshConnection.of(SshConnectionType.shell).apply(session);
            return connection.executeCommands(commands, failOnError);
        } finally {
            if (!keepAlive)
                disconnect();
        }
    }

    public List<SshCommandExecution> sftp(
        List<SshCommand> commands,
        boolean failOnError,
        boolean keepAlive) {
        try {
            assertConnected();
            SshConnection connection = SshConnection.of(SshConnectionType.sftp).apply(session);
            return connection.executeCommands(commands, failOnError);
        } finally {
            if (!keepAlive)
                disconnect();
        }
    }

    private List<SshCommand> withSourceProfiles(List<SshCommand> originalCommands) {
        String sourceCommand =
            StringUtils
                .join(
                    findProfileFiles().stream()
                        .map(p -> String.format("source %s", p))
                        .collect(Collectors.toList()),
                    " && ");
        return originalCommands
            .stream()
            .map(
                c -> new SshCommand<>(String.format("%s && %s", sourceCommand, c.getCommand())))
            .collect(Collectors.toList());
    }

    private List<String> findProfileFiles() {
        List<String> profiles =
            Lists.newArrayList(
                "/etc/profile",
                "~/.bashrc",
                "~/.bash_profile",
                "~/.bash_login",
                "~/.profile");
        SshCommand findCommand =
            new SshCommand<>(String.format("find %s", StringUtils.join(profiles, " ")));
        List<String> found =
            ((List<String>) sshExec(Lists.newArrayList(findCommand), false, false, true)
                .get(0)
                .getStdout())
                    .stream()
                    .filter(profile -> profiles.contains(profile))
                    .collect(Collectors.toList());
        return found;
    }

    public void assertConnected() {
        if (isConnected())
            return;
        connect();
    }

    public void connect() {
        try {
            session.connect();
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        session.disconnect();
    }

    public boolean isConnected() {
        return session.isConnected();
    }

    public void sendKeepAliveMsg() throws Exception {
        session.sendKeepAliveMsg();
    }

}
