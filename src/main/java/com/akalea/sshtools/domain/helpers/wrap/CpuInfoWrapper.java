package com.akalea.sshtools.domain.helpers.wrap;

import java.util.function.Supplier;

import com.akalea.sshtools.domain.helpers.CpuInfo;
import com.akalea.sshtools.domain.helpers.impl.CpuInfoHelper;
import com.akalea.sshtools.domain.session.SshSession;

public class CpuInfoWrapper implements CpuInfo {

    private Supplier<SshSession> sessionProvider;
    private boolean              keepSessionAlive = false;

    public CpuInfoWrapper(Supplier<SshSession> sessionProvider) {
        super();
        this.sessionProvider = sessionProvider;
    }

    @Override
    public Float getCpuUsage() {
        return new CpuInfoHelper(this.sessionProvider.get().connect(), keepSessionAlive).getCpuUsage();
    }
}