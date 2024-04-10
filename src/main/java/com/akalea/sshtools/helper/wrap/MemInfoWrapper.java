package com.akalea.sshtools.helper.wrap;

import java.util.function.Supplier;

import com.akalea.sshtools.domain.session.SshSession;
import com.akalea.sshtools.helper.MemInfo;
import com.akalea.sshtools.helper.impl.MemInfoHelper;

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