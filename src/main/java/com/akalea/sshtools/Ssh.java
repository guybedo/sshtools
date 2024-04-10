package com.akalea.sshtools;

import java.util.Optional;

import com.akalea.sshtools.domain.connection.SshTunnel;
import com.akalea.sshtools.domain.helpers.CpuInfo;
import com.akalea.sshtools.domain.helpers.File;
import com.akalea.sshtools.domain.helpers.MemInfo;
import com.akalea.sshtools.domain.helpers.Process;
import com.akalea.sshtools.domain.helpers.SftpExec;
import com.akalea.sshtools.domain.helpers.SshExec;
import com.akalea.sshtools.domain.helpers.impl.CpuInfoHelper;
import com.akalea.sshtools.domain.helpers.impl.FileHelper;
import com.akalea.sshtools.domain.helpers.impl.MemInfoHelper;
import com.akalea.sshtools.domain.helpers.impl.ProcessHelper;
import com.akalea.sshtools.domain.helpers.impl.SftpExecHelper;
import com.akalea.sshtools.domain.helpers.impl.SshExecHelper;
import com.akalea.sshtools.domain.helpers.wrap.CpuInfoWrapper;
import com.akalea.sshtools.domain.helpers.wrap.FileWrapper;
import com.akalea.sshtools.domain.helpers.wrap.MemInfoWrapper;
import com.akalea.sshtools.domain.helpers.wrap.SftpExecWrapper;
import com.akalea.sshtools.domain.helpers.wrap.SshExecWrapper;
import com.akalea.sshtools.domain.session.SshServerInfo;
import com.akalea.sshtools.domain.session.SshSession;
import com.akalea.sshtools.domain.session.SshSessionConfiguration;

public class Ssh {

    private SshSessionConfiguration configuration;
    private SshSession              session;
    private boolean                 keepSessionAlive;

    private Ssh() {

    }

    public static Ssh of(SshServerInfo serverInfo) {
        return of(serverInfo, false);
    }

    public static Ssh of(SshSessionConfiguration configuration) {
        return of(configuration, true);
    }

    public static Ssh of(SshServerInfo serverInfo, boolean keepSessionAlive) {
        return of(new SshSessionConfiguration().setServer(serverInfo), keepSessionAlive);
    }

    public static Ssh of(SshSessionConfiguration configuration, boolean keepSessionAlive) {
        Ssh ssh =
            new Ssh()
                .setConfiguration(configuration)
                .setKeepSessionAlive(keepSessionAlive);
        if (keepSessionAlive) {
            ssh.session = SshSession.of(configuration);
            ssh.connect();
        }
        return ssh;
    }

    public Ssh connect() {
        if (!keepSessionAlive)
            return this;
        this.session.connect();
        return this;
    }

    public boolean isConnected() {
        return this.session != null && this.session.isConnected();
    }

    public Process process() {
        return new ProcessHelper(session, keepSessionAlive);
    }

    public File file() {
        if (this.keepSessionAlive)
            return new FileHelper(session, keepSessionAlive);
        return new FileWrapper(() -> SshSession.of(configuration));
    }

    public MemInfo memInfo() {
        if (this.keepSessionAlive)
            return new MemInfoHelper(session, keepSessionAlive);
        return new MemInfoWrapper(() -> SshSession.of(configuration));
    }

    public CpuInfo cpuInfo() {
        if (this.keepSessionAlive)
            return new CpuInfoHelper(session, keepSessionAlive);
        return new CpuInfoWrapper(() -> SshSession.of(configuration));
    }

    public SshExec command() {
        if (this.keepSessionAlive)
            return new SshExecHelper(session, keepSessionAlive);
        return new SshExecWrapper(() -> SshSession.of(configuration));
    }

    public SftpExec sftp() {
        if (this.keepSessionAlive)
            return new SftpExecHelper(session, keepSessionAlive);
        return new SftpExecWrapper(() -> SshSession.of(configuration));
    }

    public SshTunnel tunnel(String remoteHost, int remotePort) {
        return Optional
            .ofNullable(session)
            .orElseGet(
                () -> SshSession
                    .of(configuration)
                    .connect())
            .tunnel(remoteHost, remotePort);
    }

    public SshSessionConfiguration getConfiguration() {
        return configuration;
    }

    public Ssh setConfiguration(SshSessionConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    public SshSession getSession() {
        return session;
    }

    private Ssh setSession(SshSession session) {
        this.session = session;
        return this;
    }

    public boolean isKeepSessionAlive() {
        return keepSessionAlive;
    }

    private Ssh setKeepSessionAlive(boolean stateless) {
        this.keepSessionAlive = stateless;
        return this;
    }

}
