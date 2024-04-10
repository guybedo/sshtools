package com.akalea.sshtools.helper;

import java.util.List;

import com.akalea.sshtools.domain.command.SshCommand;
import com.akalea.sshtools.domain.command.SshCommandExecution;

public interface SshExec {

    List<SshCommandExecution> execute(List<SshCommand> commands);

    List<SshCommandExecution> execute(
        List<SshCommand> commands,
        boolean sourceProfiles,
        boolean failOnError);

}