package com.akalea.sshtools.domain.connection;

import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akalea.sshtools.domain.command.SftpCommand;
import com.akalea.sshtools.domain.command.SshCommand;
import com.akalea.sshtools.domain.command.SshCommandExecution;
import com.akalea.sshtools.domain.command.SftpCommand.SftpCommandType;
import com.akalea.sshtools.domain.session.SshConnectionType;
import com.google.common.collect.Lists;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SftpConnection extends SshConnection {
    private final static Logger logger = LoggerFactory.getLogger(SshConnection.class);

    public SftpConnection(Session session) {
        super(session, SshConnectionType.sftp);
    }

    public SshCommandExecution executeCommand(SshCommand command) {
        try {
            if (!getChannel().isConnected())
                getChannel().connect();
            SftpCommand sftpCommand = (SftpCommand) command;
            return getCommand(sftpCommand.getType()).apply(sftpCommand);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<SshCommandExecution> executeCommands(
        List<SshCommand> commands,
        boolean failOnError) {
        List<SshCommandExecution> executions = Lists.newArrayList();
        openChannel(false);
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

    private Function<SftpCommand, SshCommandExecution> getCommand(SftpCommandType type) {
        if (SftpCommandType.rm.equals(type))
            return new Function<SftpCommand, SshCommandExecution>() {

                @Override
                public SshCommandExecution apply(SftpCommand t) {
                    SshCommandExecution execution = new SshCommandExecution(t);
                    try {
                        getChannel().rm((String) t.getArgs()[0]);
                    } catch (SftpException e) {
                        logger.error("Error", e);
                        execution.setT(e);
                    }
                    return execution;
                }
            };
        if (SftpCommandType.rename.equals(type))
            return new Function<SftpCommand, SshCommandExecution>() {

                @Override
                public SshCommandExecution apply(SftpCommand t) {
                    SshCommandExecution execution = new SshCommandExecution(t);
                    try {
                        getChannel().rename((String) t.getArgs()[0], (String) t.getArgs()[1]);
                    } catch (SftpException e) {
                        logger.error("Error", e);
                        execution.setT(e);
                    }
                    return execution;
                }
            };

        if (SftpCommandType.ls.equals(type))
            return new Function<SftpCommand, SshCommandExecution>() {

                @Override
                public SshCommandExecution apply(SftpCommand t) {
                    SshCommandExecution execution = new SshCommandExecution(t);
                    try {
                        execution.setResult(getChannel().ls((String) t.getArgs()[0]));
                    } catch (SftpException e) {
                        logger.error("Error", e);
                        execution.setT(e);
                    }
                    return execution;
                }
            };

        if (SftpCommandType.get.equals(type))
            return new Function<SftpCommand, SshCommandExecution>() {

                @Override
                public SshCommandExecution apply(SftpCommand t) {
                    SshCommandExecution execution = new SshCommandExecution(t);
                    try {
                        getChannel().get((String) t.getArgs()[0], (String) t.getArgs()[1]);
                    } catch (SftpException e) {
                        logger.error("Error", e);
                        execution.setT(e);
                    }
                    return execution;
                }
            };

        if (SftpCommandType.put.equals(type))
            return new Function<SftpCommand, SshCommandExecution>() {

                @Override
                public SshCommandExecution apply(SftpCommand t) {
                    SshCommandExecution execution = new SshCommandExecution(t);
                    try {
                        getChannel().put((String) t.getArgs()[0], (String) t.getArgs()[1]);
                    } catch (SftpException e) {
                        logger.error("Error", e);
                        execution.setT(e);
                    }
                    return execution;
                }
            };

        if (SftpCommandType.quit.equals(type))
            return new Function<SftpCommand, SshCommandExecution>() {

                @Override
                public SshCommandExecution apply(SftpCommand t) {
                    SshCommandExecution execution = new SshCommandExecution(t);
                    getChannel().quit();
                    return execution;
                }
            };

        if (SftpCommandType.chgrp.equals(type))
            return new Function<SftpCommand, SshCommandExecution>() {

                @Override
                public SshCommandExecution apply(SftpCommand t) {
                    SshCommandExecution execution = new SshCommandExecution(t);
                    try {
                        getChannel().chgrp((Integer) t.getArgs()[0], (String) t.getArgs()[1]);
                    } catch (SftpException e) {
                        logger.error("Error", e);
                        execution.setT(e);
                    }
                    return execution;
                }
            };

        if (SftpCommandType.chown.equals(type))
            return new Function<SftpCommand, SshCommandExecution>() {

                @Override
                public SshCommandExecution apply(SftpCommand t) {
                    SshCommandExecution execution = new SshCommandExecution(t);
                    try {
                        getChannel().chown((Integer) t.getArgs()[0], (String) t.getArgs()[1]);
                    } catch (SftpException e) {
                        logger.error("Error", e);
                        execution.setT(e);
                    }
                    return execution;
                }
            };

        if (SftpCommandType.chmod.equals(type))
            return new Function<SftpCommand, SshCommandExecution>() {

                @Override
                public SshCommandExecution apply(SftpCommand t) {
                    SshCommandExecution execution = new SshCommandExecution(t);
                    try {
                        getChannel().chmod((Integer) t.getArgs()[0], (String) t.getArgs()[1]);
                    } catch (SftpException e) {
                        logger.error("Error", e);
                        execution.setT(e);
                    }
                    return execution;
                }
            };

        if (SftpCommandType.rmdir.equals(type))
            return new Function<SftpCommand, SshCommandExecution>() {

                @Override
                public SshCommandExecution apply(SftpCommand t) {
                    SshCommandExecution execution = new SshCommandExecution(t);
                    try {
                        getChannel().rmdir((String) t.getArgs()[0]);
                    } catch (SftpException e) {
                        logger.error("Error", e);
                        execution.setT(e);
                    }
                    return execution;
                }
            };
        if (SftpCommandType.mkdir.equals(type))
            return new Function<SftpCommand, SshCommandExecution>() {

                @Override
                public SshCommandExecution apply(SftpCommand t) {
                    SshCommandExecution execution = new SshCommandExecution(t);
                    try {
                        getChannel().mkdir((String) t.getArgs()[0]);
                    } catch (SftpException e) {
                        logger.error("Error", e);
                        execution.setT(e);
                    }
                    return execution;
                }
            };
        if (SftpCommandType.pwd.equals(type))
            return new Function<SftpCommand, SshCommandExecution>() {

                @Override
                public SshCommandExecution apply(SftpCommand t) {
                    SshCommandExecution execution = new SshCommandExecution(t);
                    try {
                        execution.setResult(getChannel().pwd());
                    } catch (SftpException e) {
                        logger.error("Error", e);
                        execution.setT(e);
                    }
                    return execution;
                }
            };
        throw new RuntimeException("Unknown ftp command");
    }

    private ChannelSftp getChannel() {
        return (ChannelSftp) this.channel;
    }

}
