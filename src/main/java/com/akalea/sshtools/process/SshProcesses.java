package com.akalea.sshtools.process;

import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.akalea.sshtools.domain.session.SshSessionConfiguration;
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
        public List<ProcessInfo> findProcessesByName(
            SshSessionConfiguration configuration,
            String name) {
            return SshService
                .ssh(
                    configuration,
                    Lists.newArrayList(CommandHelper.processes().findProcesses(name)),
                    false,
                    false)
                .stream()
                .findFirst()
                .map(e -> (List<ProcessInfo>) e.getResult())
                .orElse(Lists.newArrayList());
        }

        public List<ProcessInfo> findProcessesByName(
            SshSessionConfiguration configuration,
            Pattern name) {
            return SshService
                .ssh(
                    configuration,
                    Lists.newArrayList(CommandHelper.processes().findProcesses(name)),
                    false,
                    false)
                .stream()
                .findFirst()
                .map(e -> (List<ProcessInfo>) e.getResult())
                .orElse(Lists.newArrayList());
        }

        public List<ProcessInfo> findProcessesByPid(
            SshSessionConfiguration configuration,
            List<String> pids) {
            return SshService
                .ssh(
                    configuration,
                    Lists.newArrayList(CommandHelper.processes().findProcesses(pids)),
                    false,
                    false)
                .stream()
                .findFirst()
                .map(e -> (List<ProcessInfo>) e.getResult())
                .orElse(Lists.newArrayList());
        }

        public void killProcessesByName(
            SshSessionConfiguration configuration,
            String name,
            int gracefulPeriodSec,
            int checkIntervalMsec) {
            List<ProcessInfo> processes = findProcessesByName(configuration, name);
            if (processes.size() == 0)
                return;

            List<String> pids =
                processes
                    .stream()
                    .map(p -> p.getPid())
                    .collect(Collectors.toList());
            killProcessesByPid(configuration, pids, gracefulPeriodSec, checkIntervalMsec);
        }

        public void killProcessesByPid(
            SshSessionConfiguration configuration,
            List<String> pids,
            int gracefulPeriodSec,
            int checkIntervalMsec) {
            sigintProcesses(configuration, pids);

            Supplier<Boolean> condition = () -> findProcessesByPid(configuration, pids).size() == 0;
            Boolean result =
                ThreadUtils.waitForCondition(
                    condition,
                    gracefulPeriodSec,
                    checkIntervalMsec);
            if (!result)
                sigkillProcesses(configuration, pids);
        }

        private void sigintProcesses(SshSessionConfiguration configuration, List<String> pids) {
            SshService
                .ssh(
                    configuration,
                    Lists.newArrayList(CommandHelper.processes().sigintProcesses(pids)),
                    false,
                    false);
        }

        private void sigkillProcesses(SshSessionConfiguration configuration, List<String> pids) {
            SshService
                .ssh(
                    configuration,
                    Lists.newArrayList(CommandHelper.processes().sigkillProcesses(pids)),
                    false,
                    false);
        }
    }

    public static Files files() {
        return new Files();
    }

    public static class Files {
        public List<FileInfo> listFiles(SshSessionConfiguration configuration, String path) {
            return SshService
                .ssh(
                    configuration,
                    Lists.newArrayList(CommandHelper.files().listFiles(path)),
                    false,
                    false)
                .stream()
                .findFirst()
                .map(e -> (List<FileInfo>) e.getResult())
                .orElse(Lists.newArrayList());
        }

        public void copyFile(
            SshSessionConfiguration configuration,
            String src,
            String dst,
            boolean force,
            boolean recursive) {
            SshService.ssh(
                configuration,
                Lists.newArrayList(CommandHelper.files().copyFile(src, dst, force, recursive)),
                false,
                false);
        }

        public void moveFile(
            SshSessionConfiguration configuration,
            String src,
            String dst,
            boolean force) {
            SshService.ssh(
                configuration,
                Lists.newArrayList(CommandHelper.files().moveFile(src, dst, force)),
                false,
                false);
        }

        public void deleteFile(
            SshSessionConfiguration configuration,
            String path,
            boolean force,
            boolean recursive) {
            SshService.ssh(
                configuration,
                Lists.newArrayList(
                    CommandHelper.files().deleteFile(path, force, recursive)),
                false,
                false);
        }
    }
}
