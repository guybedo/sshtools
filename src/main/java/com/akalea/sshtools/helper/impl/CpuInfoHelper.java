package com.akalea.sshtools.helper.impl;

import com.akalea.sshtools.domain.session.SshSession;
import com.akalea.sshtools.helper.CpuInfo;
import com.google.common.collect.Lists;

public class CpuInfoHelper implements CpuInfo {

    private SshSession session;
    private boolean    keepSessionAlive;

    public CpuInfoHelper(SshSession session, boolean keepSessionAlive) {
        super();
        this.session = session;
        this.keepSessionAlive = keepSessionAlive;
    }

    @Override
    public Float getCpuUsage() {
        return session
            .sshExec(
                Lists.newArrayList(
                    SshCommands
                        .cpuInfo()
                        .getCpuUsage()),
                keepSessionAlive)
            .stream()
            .findFirst()
            .map(e -> (Float) e.getResult())
            .orElse(null);
    }
}