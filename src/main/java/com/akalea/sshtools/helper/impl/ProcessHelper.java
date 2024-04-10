package com.akalea.sshtools.helper.impl;

import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.akalea.sshtools.domain.session.SshSession;
import com.akalea.sshtools.domain.system.ProcessInfo;
import com.akalea.sshtools.helper.Process;
import com.akalea.sshtools.utils.ThreadUtils;
import com.google.common.collect.Lists;

public class ProcessHelper implements Process {

    private SshSession session;
    private boolean    keepSessionAlive;

    public ProcessHelper(SshSession session, boolean keepSessionAlive) {
        super();
        this.session = session;
        this.keepSessionAlive = keepSessionAlive;
    }

    @Override
    public Double getProcessCpuUsage(Integer pid) {
        return session
            .sshExec(
                Lists.newArrayList(
                    SshCommands
                        .processes()
                        .getProcessCpuUsage(pid)),
                keepSessionAlive)
            .stream()
            .findFirst()
            .map(e -> ((Double) e.getResult()))
            .orElse(null);
    }

    @Override
    public Double getProcessCpuUsage(
        String commandPattern) {
        return session
            .sshExec(
                Lists.newArrayList(
                    SshCommands
                        .processes()
                        .getProcessCpuUsage(commandPattern)),
                keepSessionAlive)
            .stream()
            .findFirst()
            .map(e -> ((Double) e.getResult()))
            .orElse(null);
    }

    @Override
    public List<ProcessInfo> findProcessesByName(String name) {
        return session
            .sshExec(
                Lists.newArrayList(
                    SshCommands
                        .processes()
                        .findProcesses(name)),
                keepSessionAlive)
            .stream()
            .findFirst()
            .map(e -> (List<ProcessInfo>) e.getResult())
            .orElse(Lists.newArrayList());
    }

    @Override
    public List<ProcessInfo> findProcessesByName(Pattern name) {
        return session
            .sshExec(
                Lists.newArrayList(
                    SshCommands
                        .processes()
                        .findProcesses(name)),
                keepSessionAlive)
            .stream()
            .findFirst()
            .map(e -> (List<ProcessInfo>) e.getResult())
            .orElse(Lists.newArrayList());
    }

    @Override
    public List<ProcessInfo> findProcessesByPid(List<String> pids) {
        return session
            .sshExec(
                Lists.newArrayList(
                    SshCommands
                        .processes()
                        .findProcesses(pids)),
                keepSessionAlive)
            .stream()
            .findFirst()
            .map(e -> (List<ProcessInfo>) e.getResult())
            .orElse(Lists.newArrayList());
    }

    @Override
    public void killProcessesByName(
        String name,
        int gracefulPeriodSec,
        int checkIntervalMsec) {
        List<ProcessInfo> processes = findProcessesByName(name);
        if (processes.size() == 0)
            return;

        List<String> pids =
            processes
                .stream()
                .map(p -> p.getPid())
                .collect(Collectors.toList());
        killProcessesByPid(pids, gracefulPeriodSec, checkIntervalMsec);
    }

    @Override
    public void killProcessesByPid(
        List<String> pids,
        int gracefulPeriodSec,
        int checkIntervalMsec) {
        sigintProcesses(pids);

        Supplier<Boolean> condition = () -> findProcessesByPid(pids).size() == 0;
        Boolean result =
            ThreadUtils.waitForCondition(
                condition,
                gracefulPeriodSec,
                checkIntervalMsec);
        if (!result)
            sigkillProcesses(pids);
    }

    private void sigintProcesses(List<String> pids) {
        session
            .sshExec(
                Lists.newArrayList(
                    SshCommands
                        .processes()
                        .sigintProcesses(pids)),
                keepSessionAlive);
    }

    private void sigkillProcesses(List<String> pids) {
        session
            .sshExec(
                Lists.newArrayList(
                    SshCommands
                        .processes()
                        .sigkillProcesses(pids)),
                keepSessionAlive);
    }
}