package com.akalea.sshtools.domain.connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import com.akalea.sshtools.domain.command.SshCommand;
import com.akalea.sshtools.domain.command.SshCommandExecution;
import com.akalea.sshtools.domain.session.SshConnectionType;
import com.google.common.collect.Lists;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;

public class SshExecConnection extends SshConnection<SshCommand, SshCommandExecution> {

    public SshExecConnection(Session session) {
        super(session, SshConnectionType.command);
    }

    public SshCommandExecution executeCommand(SshCommand command) {
        try {
            openChannel(false);
            getChannel().setCommand(command.getCommand());
            getChannel().setInputStream(null);
            InputStream in = getChannel().getInputStream();
            InputStream err = getChannel().getErrStream();
            getChannel().connect();
            SshCommandExecution execution = result(command, in, err);
            getChannel().disconnect();
            return execution;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<SshCommandExecution> executeCommands(
        List<SshCommand> commands,
        boolean failOnError) {
        List<SshCommandExecution> executions = Lists.newArrayList();
        for (SshCommand sshCommand : commands) {
            try {
                SshCommandExecution execution = executeCommand(sshCommand);
                executions.add(execution);
                if (execution.isError() && failOnError)
                    break;
            } catch (Exception e) {
                if (failOnError)
                    break;
            }
        }
        return executions;
    }

    private SshCommandExecution result(
        SshCommand command,
        InputStream in,
        InputStream err) {
        List<String> stdouts = readInput(in);
        List<String> stderrs = readErr(err);
        Object result =
            Optional
                .ofNullable(command.getResultExtractor())
                .map(extractor -> extractor.apply(stdouts))
                .orElse(null);
        SshCommandExecution execution = new SshCommandExecution(command);
        execution.setResult(stdouts, stderrs, result);
        return execution;
    }

    protected List<String> readErr() {
        try {
            return readErr(getChannel().getErrStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected List<String> readErr(InputStream err) {
        try {
            return readLines(err);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ChannelExec getChannel() {
        return (ChannelExec) this.channel;
    }

}
