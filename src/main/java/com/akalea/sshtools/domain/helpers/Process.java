package com.akalea.sshtools.domain.helpers;

import java.util.List;
import java.util.regex.Pattern;

import com.akalea.sshtools.domain.system.ProcessInfo;

public interface Process {

    Double getProcessCpuUsage(Integer pid);

    Double getProcessCpuUsage(
        String commandPattern);

    List<ProcessInfo> findProcessesByName(String name);

    List<ProcessInfo> findProcessesByName(Pattern name);

    List<ProcessInfo> findProcessesByPid(List<String> pids);

    void killProcessesByName(
        String name,
        int gracefulPeriodSec,
        int checkIntervalMsec);

    void killProcessesByPid(
        List<String> pids,
        int gracefulPeriodSec,
        int checkIntervalMsec);

}