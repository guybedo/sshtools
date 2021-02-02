package com.akalea.sshtools.domain.connection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akalea.sshtools.domain.command.SshCommand;
import com.akalea.sshtools.domain.command.SshCommandExecution;
import com.akalea.sshtools.domain.session.SshConnectionType;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;

public abstract class SshConnection {

    private final static Logger logger = LoggerFactory.getLogger(SshConnection.class);

    protected SshConnectionType type;

    protected Session session;
    protected Channel channel;

    public SshConnection(Session session, SshConnectionType type) {
        super();
        this.session = session;
        this.type = type;
    }

    public abstract List<SshCommandExecution> executeCommands(
        List<SshCommand> commands,
        boolean failOnError);

    protected void openChannel(boolean useCache) {
        try {
            if (channel == null || !useCache)
                channel = session.openChannel(type.getChannelType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static Function<Session, SshConnection> of(SshConnectionType type) {
        if (SshConnectionType.shell.equals(type))
            return session -> new SshShellConnection(session);
        if (SshConnectionType.command.equals(type))
            return session -> new SshExecConnection(session);
        if (SshConnectionType.sftp.equals(type))
            return session -> new SftpConnection(session);
        if (SshConnectionType.tunnel.equals(type))
            return session -> new SshTunnel(session);
        throw new RuntimeException("Unknown channel type");
    }

    protected List<String> readInput() {
        try {
            return readInput(channel.getInputStream());
        } catch (Exception e) {
            logger.error("Error", e);
            throw new RuntimeException(e);
        }
    }

    protected List<String> readInput(InputStream in) {
        try {
            return readLines(in);
        } catch (IOException e) {
            logger.error("Error", e);
            throw new RuntimeException(e);
        }
    }

    protected List<String> readLines(InputStream in) throws IOException {
        return IOUtils.readLines(in, Charset.defaultCharset());
    }

    protected List<String> toLines(ByteArrayOutputStream out) throws IOException {
        return readLines(new ByteArrayInputStream(out.toByteArray()));
    }

}
