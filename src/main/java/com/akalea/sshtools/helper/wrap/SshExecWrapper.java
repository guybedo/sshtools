package com.akalea.sshtools.helper.wrap;

import java.util.List;
import java.util.function.Supplier;

import com.akalea.sshtools.domain.command.SshCommand;
import com.akalea.sshtools.domain.command.SshCommandExecution;
import com.akalea.sshtools.domain.session.SshSession;
import com.akalea.sshtools.helper.SshExec;
import com.akalea.sshtools.helper.impl.SshExecHelper;

public class SshExecWrapper implements SshExec {

    private Supplier<SshSession> sessionProvider;
    private boolean              keepSessionAlive = false;

    public SshExecWrapper(Supplier<SshSession> sessionProvider) {
        super();
        this.sessionProvider = sessionProvider;
    }

    @Override
    public List<SshCommandExecution> execute(List<SshCommand> commands) {
        return new SshExecHelper(this.sessionProvider.get().connect(), keepSessionAlive).execute(commands);
    }

    @Override
    public List<SshCommandExecution> execute(
        List<SshCommand> commands,
        boolean sourceProfiles,
        boolean failOnError) {
        return new SshExecHelper(this.sessionProvider.get().connect(), keepSessionAlive).execute(
            commands,
            sourceProfiles,
            failOnError);
    }

}