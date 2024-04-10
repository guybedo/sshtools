package com.akalea.sshtools.domain.connection;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akalea.sshtools.domain.command.SshCommand;
import com.akalea.sshtools.domain.command.SshCommandExecution;
import com.akalea.sshtools.domain.session.SshConnectionType;
import com.jcraft.jsch.Session;

public class SshTunnel extends SshConnection<SshCommand, SshCommandExecution> {

    private final static Logger logger = LoggerFactory.getLogger(SshTunnel.class);

    private String  remoteHost;
    private Integer remotePort;

    private Integer localPort;

    public SshTunnel(Session session) {
        super(session, SshConnectionType.tunnel);
    }

    public void close() {
        try {
            if (this.session.isConnected()) {
                this.disconnectTunnel();
                this.session.disconnect();
            }
        } catch (Exception e) {
            logger.error("Could not disconnect session", e);
            throw new RuntimeException(e);
        }
    }

    public SshTunnel setupTunnel(String remoteHost, int remotePort) {
        if (this.localPort != null)
            throw new RuntimeException(
                String.format(
                    "Tunnel already setup: %d => %s:%d",
                    localPort,
                    remoteHost,
                    remotePort));
        try {
            this.localPort = this.session.setPortForwardingL(0, remoteHost, remotePort);
            return this;
        } catch (Exception e) {
            logger.error("Error setting up tunnel", e);
            throw new RuntimeException(e);
        }
    }

    public SshTunnel disconnectTunnel() {
        if (this.localPort == null) {
            logger.warn("No tunnel setup yet");
            return this;
        }
        try {
            this.session.delPortForwardingL(localPort);
            setRemoteHost(null)
                .setRemotePort(null)
                .setLocalPort(null);
            return this;
        } catch (Exception e) {
            logger.error("Error setting up tunnel", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SshCommandExecution> executeCommands(
        List<SshCommand> commands,
        boolean failOnError) {
        throw new RuntimeException("Not Implemented");
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public SshTunnel setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
        return this;
    }

    public Integer getRemotePort() {
        return remotePort;
    }

    public SshTunnel setRemotePort(Integer remotePort) {
        this.remotePort = remotePort;
        return this;
    }

    public Integer getLocalPort() {
        return localPort;
    }

    public SshTunnel setLocalPort(Integer localPort) {
        this.localPort = localPort;
        return this;
    }

}
