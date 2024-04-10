package com.akalea.sshtools.examples.ssh;

import com.akalea.sshtools.Ssh;
import com.akalea.sshtools.domain.session.SshServerInfo;

public class DeleteFile {

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
        Ssh
            .of(serverInfo)
            .file()
            .deleteFile("/home/user/test.txt");

    }
}
