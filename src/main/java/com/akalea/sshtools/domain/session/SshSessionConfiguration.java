package com.akalea.sshtools.domain.session;

import java.util.Map;

public class SshSessionConfiguration {

    private SshServerInfo       server;
    private Map<String, String> knownHosts;
    private int                 timeout;

    public SshServerInfo getServer() {
        return server;
    }

    public SshSessionConfiguration setServer(SshServerInfo server) {
        this.server = server;
        return this;
    }

    public Map<String, String> getKnownHosts() {
        return knownHosts;
    }

    public SshSessionConfiguration setKnownHosts(Map<String, String> knownHosts) {
        this.knownHosts = knownHosts;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public SshSessionConfiguration setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

}
