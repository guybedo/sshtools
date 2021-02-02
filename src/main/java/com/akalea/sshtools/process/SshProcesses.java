package com.akalea.sshtools.process;

import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.akalea.sshtools.domain.session.SshServerInfo;
import com.akalea.sshtools.domain.system.FileInfo;
import com.akalea.sshtools.domain.system.ProcessInfo;
import com.akalea.sshtools.helper.CommandHelper;
import com.akalea.sshtools.service.SshService;
import com.akalea.sshtools.utils.ThreadUtils;
import com.google.common.collect.Lists;

public class SshProcesses {

    public static Processes processes() {
        return new Processes();
    }

    public static class Processes {
        public List<ProcessInfo> findProcessesByName(SshServerInfo serverInfo, String name) {
            return SshService
                .ssh(
                    serverInfo,
                    Lists.newArrayList(CommandHelper.processes().findProcesses(name)),
                    false,
                    false)
                .stream()
                .findFirst()
                .map(e -> (List<ProcessInfo>) e.getResult())
                .orElse(Lists.newArrayList());
        }
        
        public List<ProcessInfo> findProcessesByName(SshServerInfo serverInfo, Pattern name) {
            return SshService
                .ssh(
                    serverInfo,
                    Lists.newArrayList(CommandHelper.processes().findProcesses(name)),
                    false,
                    false)
                .stream()
                .findFirst()
                .map(e -> (List<ProcessInfo>) e.getResult())
                .orElse(Lists.newArrayList());
        }

        public List<ProcessInfo> findProcessesByPid(SshServerInfo serverInfo, List<String> pids) {
            return SshService
                .ssh(
                    serverInfo,
                    Lists.newArrayList(CommandHelper.processes().findProcesses(pids)),
                    false,
                    false)
                .stream()
                .findFirst()
                .map(e -> (List<ProcessInfo>) e.getResult())
                .orElse(Lists.newArrayList());
        }

        public void killProcessesByName(
            SshServerInfo serverInfo,
            String name,
            int gracefulPeriodSec,
            int checkIntervalMsec) {
            List<ProcessInfo> processes = findProcessesByName(serverInfo, name);
            if (processes.size() == 0)
                return;

            List<String> pids =
                processes
                    .stream()
                    .map(p -> p.getPid())
                    .collect(Collectors.toList());
            killProcessesByPid(serverInfo, pids, gracefulPeriodSec, checkIntervalMsec);
        }

        public void killProcessesByPid(
            SshServerInfo serverInfo,
            List<String> pids,
            int gracefulPeriodSec,
            int checkIntervalMsec) {
            sigintProcesses(serverInfo, pids);

            Supplier<Boolean> condition = () -> findProcessesByPid(serverInfo, pids).size() == 0;
            Boolean result =
                ThreadUtils.waitForCondition(
                    condition,
                    gracefulPeriodSec,
                    checkIntervalMsec);
            if (!result)
                sigkillProcesses(serverInfo, pids);
        }

        private void sigintProcesses(SshServerInfo serverInfo, List<String> pids) {
            SshService
                .ssh(
                    serverInfo,
                    Lists.newArrayList(CommandHelper.processes().sigintProcesses(pids)),
                    false,
                    false);
        }

        private void sigkillProcesses(SshServerInfo serverInfo, List<String> pids) {
            SshService
                .ssh(
                    serverInfo,
                    Lists.newArrayList(CommandHelper.processes().sigkillProcesses(pids)),
                    false,
                    false);
        }
    }

    public static Files files() {
        return new Files();
    }

    public static class Files {
        public List<FileInfo> listFiles(SshServerInfo serverInfo, String path) {
            return SshService
                .ssh(
                    serverInfo,
                    Lists.newArrayList(CommandHelper.files().listFiles(path)),
                    false,
                    false)
                .stream()
                .findFirst()
                .map(e -> (List<FileInfo>) e.getResult())
                .orElse(Lists.newArrayList());
        }

        public void copyFile(
            SshServerInfo serverInfo,
            String src,
            String dst,
            boolean force,
            boolean recursive) {
            SshService.ssh(
                serverInfo,
                Lists.newArrayList(CommandHelper.files().copyFile(src, dst, force, recursive)),
                false,
                false);
        }

        public void moveFile(
            SshServerInfo serverInfo,
            String src,
            String dst,
            boolean force) {
            SshService.ssh(
                serverInfo,
                Lists.newArrayList(CommandHelper.files().moveFile(src, dst, force)),
                false,
                false);
        }

        public void deleteFile(
            SshServerInfo serverInfo,
            String path,
            boolean force,
            boolean recursive) {
            SshService.ssh(
                serverInfo,
                Lists.newArrayList(
                    CommandHelper.files().deleteFile(path, force, recursive)),
                false,
                false);
        }
    }
}
