package com.akalea.sshtools.helper.impl;

import com.akalea.sshtools.domain.session.SshSession;
import com.akalea.sshtools.helper.MemInfo;
import com.google.common.collect.Lists;

public class MemInfoHelper implements MemInfo {

    private SshSession session;
    private boolean    keepSessionAlive;

    public MemInfoHelper(SshSession session, boolean keepSessionAlive) {
        super();
        this.session = session;
        this.keepSessionAlive = keepSessionAlive;
    }

    @Override
    public Integer getAvailableMemory() {
        return session
            .sshExec(
                Lists.newArrayList(
                    SshCommands
                        .memInfo()
                        .getAvailableMemory()),
                keepSessionAlive)
            .stream()
            .findFirst()
            .map(e -> (Integer) e.getResult())
            .orElse(null);
    }
}