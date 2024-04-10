package com.akalea.sshtools.domain.helpers.wrap;

import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import com.akalea.sshtools.domain.helpers.Process;
import com.akalea.sshtools.domain.helpers.impl.ProcessHelper;
import com.akalea.sshtools.domain.session.SshSession;
import com.akalea.sshtools.domain.system.ProcessInfo;

public class ProcessWrapper implements Process {

    private Supplier<SshSession> sessionProvider;
    private boolean              keepSessionAlive = false;

    public ProcessWrapper(Supplier<SshSession> sessionProvider) {
        super();
        this.sessionProvider = sessionProvider;
    }

    @Override
    public Double getProcessCpuUsage(Integer pid) {
        return new ProcessHelper(this.sessionProvider.get().connect(), keepSessionAlive).getProcessCpuUsage(
            pid);
    }

    @Override
    public Double getProcessCpuUsage(String commandPattern) {
        return new ProcessHelper(this.sessionProvider.get().connect(), keepSessionAlive).getProcessCpuUsage(
            commandPattern);
    }

    @Override
    public List<ProcessInfo> findProcessesByName(String name) {
        return new ProcessHelper(this.sessionProvider.get().connect(), keepSessionAlive).findProcessesByName(
            name);
    }

    @Override
    public List<ProcessInfo> findProcessesByName(Pattern name) {
        return new ProcessHelper(this.sessionProvider.get().connect(), keepSessionAlive).findProcessesByName(
            name);
    }

    @Override
    public List<ProcessInfo> findProcessesByPid(List<String> pids) {
        return new ProcessHelper(this.sessionProvider.get().connect(), keepSessionAlive).findProcessesByPid(
            pids);
    }

    @Override
    public void killProcessesByName(
        String name,
        int gracefulPeriodSec,
        int checkIntervalMsec) {
        new ProcessHelper(this.sessionProvider.get().connect(), keepSessionAlive).killProcessesByName(
            name,
            gracefulPeriodSec,
            checkIntervalMsec);
    }

    @Override
    public void killProcessesByPid(
        List<String> pids,
        int gracefulPeriodSec,
        int checkIntervalMsec) {
        new ProcessHelper(this.sessionProvider.get().connect(), keepSessionAlive).findProcessesByPid(pids);
    }

}