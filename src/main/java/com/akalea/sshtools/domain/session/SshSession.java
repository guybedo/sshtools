package com.akalea.sshtools.domain.session;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.akalea.sshtools.domain.command.SshCommand;
import com.akalea.sshtools.domain.command.SshCommandExecution;
import com.akalea.sshtools.domain.connection.SshConnection;
import com.akalea.sshtools.domain.connection.SshTunnel;
import com.akalea.sshtools.domain.exception.SshException;
import com.google.common.collect.Lists;
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

    public static SshSession of(SshSessionConfiguration configuration) {
        SshServerInfo server = configuration.getServer();
        SshSession session = null;
        if (server.isUserPasswordAuth()) {
            session = sshSessionWithUserPassword(configuration);
        } else if (server.isPrivateKeyFile() && !server.isPublicKeyFile()) {
            session =
                sshSession(
                    configuration,
                    server.getKey().getPrivateKeyFile(),
                    server.getKey().getPassphrase());
        } else {
            String privateKey =
                Optional
                    .ofNullable(server.getKey().getPrivateKey())
                    .orElseGet(() -> {
                        try {
                            return FileUtils.readFileToString(
                                new File(server.getKey().getPrivateKeyFile()),
                                Charset.defaultCharset());
                        } catch (Exception e) {
                            throw new RuntimeException("Error reading private key file", e);
                        }
                    });
            String publicKey =
                Optional
                    .ofNullable(server.getKey().getPublicKey())
                    .orElseGet(() -> {

                        String publicKeyFilename =
                            Optional
                                .ofNullable(server.getKey().getPublicKeyFile())
                                .orElse(
                                    String.format("%s.pub", server.getKey().getPrivateKeyFile()));
                        File publicKeyFile = new File(publicKeyFilename);
                        return Optional
                            .ofNullable(publicKeyFile)
                            .filter(f -> f.exists())
                            .map(
                                f -> {
                                    try {
                                        return FileUtils.readFileToString(
                                            f,
                                            Charset.defaultCharset());
                                    } catch (Exception e) {
                                        throw new RuntimeException(
                                            "Error reading public key file",
                                            e);
                                    }
                                })
                            .orElse(null);

                    });
            session =
                sshSession(
                    configuration,
                    privateKey,
                    publicKey,
                    server.getKey().getPassphrase());
        }
        return session;
    }

    private static SshSession sshSession(
        SshSessionConfiguration configuration,
        String privateKeyFile,
        String passphrase) {
        try {
            JSch jsch = new JSch();
            setKnownHosts(jsch, configuration);
            jsch.addIdentity(privateKeyFile, passphrase);
            Session session =
                jsch.getSession(
                    configuration.getServer().getUsername(),
                    configuration.getServer().getHost(),
                    configuration.getServer().getPort());
            return new SshSession(session);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static SshSession sshSession(
        SshSessionConfiguration configuration,
        String privateKey,
        String publicKey,
        String passphrase) {
        try {
            JSch jsch = new JSch();
            setKnownHosts(jsch, configuration);
            byte[] passBytes =
                Optional
                    .ofNullable(passphrase)
                    .map(p -> p.getBytes(Charset.forName("UTF-8")))
                    .orElse(null);

            if (!StringUtils.isEmpty(publicKey))
                jsch.addIdentity(
                    configuration.getServer().getUsername(),
                    privateKey.getBytes(Charset.forName("UTF-8")),
                    publicKey.getBytes(Charset.forName("UTF-8")),
                    passBytes);
            else
                jsch.addIdentity(privateKey, passBytes);
            Session session =
                jsch.getSession(
                    configuration.getServer().getUsername(),
                    configuration.getServer().getHost(),
                    configuration.getServer().getPort());
            return new SshSession(session);
        } catch (Exception e) {
            throw SshException.fromJSchException((JSchException) e);
        }
    }

    private static SshSession sshSessionWithUserPassword(SshSessionConfiguration configuration) {
        try {
            JSch jsch = new JSch();
            Session session =
                jsch.getSession(
                    configuration.getServer().getUsername(),
                    configuration.getServer().getHost(),
                    configuration.getServer().getPort());
            session.setPassword(configuration.getServer().getPassword());
            setKnownHosts(jsch, configuration);
            return new SshSession(session);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setKnownHosts(JSch jsch, SshSessionConfiguration configuration) {
        Optional
            .ofNullable(configuration.getKnownHosts())
            .ifPresent(h -> {
                try {
                    jsch.setKnownHosts(
                        IOUtils.toInputStream(
                            StringUtils.join(
                                h
                                    .entrySet()
                                    .stream()
                                    .map(e -> String.format("%s\t%s", e.getKey(), e.getValue()))
                                    .collect(Collectors.toList()),
                                "\r")));
                } catch (Exception e) {
                    throw new RuntimeException("Could not set known hosts", e);
                }
            });
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

    public SshTunnel tunnel(String remoteHost, int remotePort) {
        assertConnected();
        SshTunnel tunnel =
            (SshTunnel) SshConnection
                .of(SshConnectionType.tunnel)
                .apply(session);
        return tunnel.setupTunnel(remoteHost, remotePort);
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

    public SshSession setTimeout(int timeout) {
        try {
            session.setTimeout(timeout);
            return this;
        } catch (Exception e) {
            throw new RuntimeException("Could not set session timeout");
        }
    }

    public void sendKeepAliveMsg() throws Exception {
        session.sendKeepAliveMsg();
    }

}
