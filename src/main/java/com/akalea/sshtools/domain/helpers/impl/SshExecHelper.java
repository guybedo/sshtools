package com.akalea.sshtools.domain.helpers.impl;

import java.util.List;

import com.akalea.sshtools.domain.command.SshCommand;
import com.akalea.sshtools.domain.command.SshCommandExecution;
import com.akalea.sshtools.domain.helpers.SshExec;
import com.akalea.sshtools.domain.session.SshSession;

public class SshExecHelper implements SshExec {

    private SshSession session;
    private boolean    keepSessionAlive;

    public SshExecHelper(SshSession session, boolean keepSessionAlive) {
        super();
        this.session = session;
        this.keepSessionAlive = keepSessionAlive;
    }

    @Override
    public List<SshCommandExecution> execute(List<SshCommand> commands) {
        return execute(commands, false, false);
    }

    @Override
    public List<SshCommandExecution> execute(
        List<SshCommand> commands,
        boolean sourceProfiles,
        boolean failOnError) {
        return session.sshExec(commands, sourceProfiles, failOnError, keepSessionAlive);
    }

}