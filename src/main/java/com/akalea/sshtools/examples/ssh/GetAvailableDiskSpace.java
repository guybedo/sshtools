package com.akalea.sshtools.examples.ssh;

import com.akalea.sshtools.Ssh;
import com.akalea.sshtools.domain.session.SshServerInfo;

public class GetAvailableDiskSpace {

    public static void main(String[] args) {
        String username = "";
        String host = "";
        String privateKeyFilePath = "";
        SshServerInfo serverInfo =
            new SshServerInfo(
                username,
                host,
                privateKeyFilePath,
                null);

        String path = "/";
        Double result =
                Ssh
                .of(serverInfo)
                .file()
                .getAvailableDiskSpace(path);
        System.out.println(String.format("Available %f", result));

    }
}
