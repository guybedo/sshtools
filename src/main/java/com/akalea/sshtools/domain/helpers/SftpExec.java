package com.akalea.sshtools.domain.helpers;

import java.util.List;

import com.akalea.sshtools.domain.command.SftpCommand;
import com.akalea.sshtools.domain.command.SshCommandExecution;

public interface SftpExec {

    List<SshCommandExecution> execute(List<SftpCommand> commands);

    List<SshCommandExecution> execute(List<SftpCommand> commands, boolean failOnError);

}