package com.akalea.sshtools.examples.tunnel;

import com.akalea.sshtools.Ssh;
import com.akalea.sshtools.domain.connection.SshTunnel;
import com.akalea.sshtools.domain.session.SshServerInfo;

public class Tunnel {

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

        String remoteHost = "";
        int remotePort = 8080;

        SshTunnel tunnel =
            Ssh
                .of(serverInfo)
                .tunnel(remoteHost, remotePort);
        System.out.println(String.format("Local port: %d", tunnel.getLocalPort()));

    }
}
