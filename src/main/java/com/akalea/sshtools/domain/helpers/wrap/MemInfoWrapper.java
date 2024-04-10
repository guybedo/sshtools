package com.akalea.sshtools.domain.helpers.wrap;

import java.util.function.Supplier;

import com.akalea.sshtools.domain.helpers.MemInfo;
import com.akalea.sshtools.domain.helpers.impl.MemInfoHelper;
import com.akalea.sshtools.domain.session.SshSession;

public class MemInfoWrapper implements MemInfo {

    private Supplier<SshSession> sessionProvider;
    private boolean              keepSessionAlive = false;

    public MemInfoWrapper(Supplier<SshSession> sessionProvider) {
        super();
        this.sessionProvider = sessionProvider;
    }

    @Override
    public Integer getAvailableMemory() {
        return new MemInfoHelper(this.sessionProvider.get().connect(), keepSessionAlive).getAvailableMemory();
    }
}