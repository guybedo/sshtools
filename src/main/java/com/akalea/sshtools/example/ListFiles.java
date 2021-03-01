package com.akalea.sshtools.example;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.akalea.sshtools.domain.session.SshServerInfo;
import com.akalea.sshtools.domain.session.SshSessionConfiguration;
import com.akalea.sshtools.domain.system.FileInfo;
import com.akalea.sshtools.helper.CommandHelper;
import com.akalea.sshtools.service.SshService;
import com.google.common.collect.Lists;

public class ListFiles {

    public static void main(String[] args) {
        SshServerInfo serverInfo =
            new SshServerInfo(
                "login",
                "localhost",
                "/home/user/.ssh/id_rsa",
                null);
        SshService
            .ssh(
                new SshSessionConfiguration().setServer(serverInfo),
                Lists.newArrayList(CommandHelper.files().listFiles("/home/user")),
                false,
                false)
            .stream()
            .forEach(execution -> {
                if (execution.getStderr() != null && !execution.getStderr().isEmpty())
                    System.out.println(StringUtils.join(execution.getStderr(), "\n"));
                else {
                    List<FileInfo> files = ((List<FileInfo>) execution.getResult());
                    System.out.println(
                        String.format(
                            "Found %d files %s",
                            files.size(),
                            files
                                .stream()
                                .map(p -> p.toString())
                                .reduce((a, b) -> String.format("%s,%s", a, b))
                                .get()));
                }

            });
        ;

    }
}
