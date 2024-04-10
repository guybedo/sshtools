package com.akalea.sshtools.helper.impl;

import java.util.List;

import com.akalea.sshtools.domain.command.SftpCommand;
import com.akalea.sshtools.domain.command.SshCommandExecution;
import com.akalea.sshtools.domain.session.SshSession;
import com.akalea.sshtools.helper.SftpExec;

public class SftpExecHelper implements SftpExec {

    private SshSession session;
    private boolean    keepSessionAlive;

    public SftpExecHelper(SshSession session, boolean keepSessionAlive) {
        super();
        this.session = session;
        this.keepSessionAlive = keepSessionAlive;
    }

    @Override
    public List<SshCommandExecution> execute(List<SftpCommand> commands) {
        return execute(commands, false);
    }

    @Override
    public List<SshCommandExecution> execute(List<SftpCommand> commands, boolean failOnError) {
        return session.sftp(commands, failOnError, keepSessionAlive);
    }

}