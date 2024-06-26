package com.akalea.sshtools.domain.helpers.wrap;

import java.util.List;
import java.util.function.Supplier;

import com.akalea.sshtools.domain.command.SftpCommand;
import com.akalea.sshtools.domain.command.SshCommandExecution;
import com.akalea.sshtools.domain.helpers.SftpExec;
import com.akalea.sshtools.domain.helpers.impl.SftpExecHelper;
import com.akalea.sshtools.domain.session.SshSession;

public class SftpExecWrapper implements SftpExec {

    private Supplier<SshSession> sessionProvider;
    private boolean              keepSessionAlive = false;

    public SftpExecWrapper(Supplier<SshSession> sessionProvider) {
        super();
        this.sessionProvider = sessionProvider;
    }

    @Override
    public List<SshCommandExecution> execute(List<SftpCommand> commands) {
        return new SftpExecHelper(this.sessionProvider.get().connect(), keepSessionAlive).execute(commands);
    }

    @Override
    public List<SshCommandExecution> execute(List<SftpCommand> commands, boolean failOnError) {
        return new SftpExecHelper(this.sessionProvider.get().connect(), keepSessionAlive).execute(
            commands,
            failOnError);
    }

}