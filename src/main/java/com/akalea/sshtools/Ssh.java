package com.akalea.sshtools;

import com.akalea.sshtools.domain.connection.SshTunnel;
import com.akalea.sshtools.domain.session.SshServerInfo;
import com.akalea.sshtools.domain.session.SshSession;
import com.akalea.sshtools.domain.session.SshSessionConfiguration;
import com.akalea.sshtools.helper.CpuInfo;
import com.akalea.sshtools.helper.File;
import com.akalea.sshtools.helper.MemInfo;
import com.akalea.sshtools.helper.Process;
import com.akalea.sshtools.helper.SftpExec;
import com.akalea.sshtools.helper.SshExec;
import com.akalea.sshtools.helper.impl.CpuInfoHelper;
import com.akalea.sshtools.helper.impl.FileHelper;
import com.akalea.sshtools.helper.impl.MemInfoHelper;
import com.akalea.sshtools.helper.impl.ProcessHelper;
import com.akalea.sshtools.helper.impl.SftpExecHelper;
import com.akalea.sshtools.helper.impl.SshExecHelper;
import com.akalea.sshtools.helper.wrap.CpuInfoWrapper;
import com.akalea.sshtools.helper.wrap.FileWrapper;
import com.akalea.sshtools.helper.wrap.MemInfoWrapper;
import com.akalea.sshtools.helper.wrap.SftpExecWrapper;
import com.akalea.sshtools.helper.wrap.SshExecWrapper;

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

    public static SshTunnel tunnel(
        SshServerInfo serverInfo,
        String remoteHost,
        int remotePort) {
        return tunnel(new SshSessionConfiguration().setServer(serverInfo), remoteHost, remotePort);
    }

    public static SshTunnel tunnel(
        SshSessionConfiguration configuration,
        String remoteHost,
        int remotePort) {
        return SshSession
            .of(configuration)
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
