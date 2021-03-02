package com.akalea.sshtools.helper;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.akalea.sshtools.domain.command.SshCommand;
import com.akalea.sshtools.domain.system.FileInfo;
import com.akalea.sshtools.domain.system.ProcessInfo;
import com.google.common.collect.Lists;

public class CommandHelper {

    public static Files files() {
        return new Files();
    }

    public static Processes processes() {
        return new Processes();
    }

    public static class Processes {

        public SshCommand getProcessCpuUsage(Integer pid) {
            return new SshCommand<Double>(
                String.format(
                    "top -b -n 2 -d 0.2 -p %d | tail -2 | head -1 | awk '{print $9}'",
                    pid),
                null,
                new Function<List<String>, Double>() {

                    @Override
                    public Double apply(List<String> stdouts) {
                        if (stdouts == null || stdouts.size() == 0)
                            return null;
                        return Double.parseDouble(stdouts.get(0));
                    }
                });
        }

        public SshCommand getProcessCpuUsage(String commandPattern) {
            return new SshCommand<Double>(
                String.format(
                    "top -b -n 2 -d 0.2 -p $(pgrep -f %s | head -1) | tail -2 | head -1 | awk '{print $9}'",
                    commandPattern),
                null,
                new Function<List<String>, Double>() {

                    @Override
                    public Double apply(List<String> stdouts) {
                        if (stdouts == null || stdouts.size() == 0)
                            return null;
                        return Double.parseDouble(stdouts.get(0));
                    }
                });
        }

        public SshCommand findProcesses(String name) {
            return new SshCommand<List<ProcessInfo>>(
                String.format("ps -eo pid,args | grep -v grep | grep \"%s\"", name),
                null,
                new Function<List<String>, List<ProcessInfo>>() {

                    @Override
                    public List<ProcessInfo> apply(List<String> stdouts) {
                        if (stdouts == null || stdouts.size() == 0)
                            return Lists.newArrayList();
                        return stdouts.stream().map(line -> {
                            String[] data = line.trim().split(" ");
                            String args =
                                StringUtils.join(
                                    Stream.of(data).skip(1).collect(Collectors.toList()),
                                    " ");
                            return new ProcessInfo(data[0], args);
                        }).filter(processInfo -> processInfo != null).collect(Collectors.toList());
                    }
                });
        }

        public SshCommand findProcesses(Pattern pattern) {
            return new SshCommand<List<ProcessInfo>>(
                "ps -eo pid,args",
                null,
                new Function<List<String>, List<ProcessInfo>>() {

                    @Override
                    public List<ProcessInfo> apply(List<String> stdouts) {
                        if (stdouts == null || stdouts.size() == 0)
                            return Lists.newArrayList();
                        return stdouts
                            .stream()
                            .filter(line -> pattern.matcher(line).matches())
                            .map(line -> {
                                String[] data = line.trim().split(" ");
                                String args =
                                    StringUtils.join(
                                        Stream.of(data).skip(1).collect(Collectors.toList()),
                                        " ");
                                return new ProcessInfo(data[0], args);
                            })
                            .filter(processInfo -> processInfo != null)
                            .collect(Collectors.toList());
                    }
                });
        }

        public SshCommand findProcesses(List<String> pids) {
            return new SshCommand<List<ProcessInfo>>(
                String.format("ps -p %s -o pid,args", StringUtils.join(pids, ",")),
                null,
                new Function<List<String>, List<ProcessInfo>>() {

                    @Override
                    public List<ProcessInfo> apply(List<String> stdouts) {
                        if (stdouts == null || stdouts.size() == 0)
                            return Lists.newArrayList();
                        return stdouts.stream().map(line -> {
                            String[] data = line.split(" ");
                            String args =
                                StringUtils.join(
                                    Stream.of(data).skip(1).collect(Collectors.toList()),
                                    " ");
                            return new ProcessInfo(data[0], args);
                        }).filter(processInfo -> processInfo != null).collect(Collectors.toList());
                    }
                });
        }

        public SshCommand sigintProcesses(List<String> pids) {
            return new SshCommand<List<ProcessInfo>>(
                String.format("kill %s", StringUtils.join(pids, " ")));
        }

        public SshCommand sigkillProcesses(List<String> pids) {
            return new SshCommand<List<ProcessInfo>>(
                String.format("kill -9 %s", StringUtils.join(pids, " ")));
        }
    }

    public static class Files {

        public SshCommand listFiles(String path) {
            return new SshCommand<List<FileInfo>>(
                String.format("ls -alL %s", path),
                null,
                new Function<List<String>, List<FileInfo>>() {

                    @Override
                    public List<FileInfo> apply(List<String> stdouts) {
                        if (stdouts == null || stdouts.size() == 0)
                            return Lists.newArrayList();
                        return stdouts.stream()
                            .map(line -> FileInfo.fromStdout(path, line))
                            .filter(fileInfo -> fileInfo != null)
                            .collect(Collectors.toList());
                    }
                });
        }

        public SshCommand copyFile(String src, String dst, boolean force, boolean recursive) {
            List<String> options = Lists.newArrayList();
            if (force)
                options.add("f");
            if (recursive)
                options.add("R");
            StringBuilder commandBuilder = new StringBuilder().append("cp");
            if (options.size() > 0)
                commandBuilder.append(" ").append("-").append(StringUtils.join(options, ""));
            commandBuilder.append(" ").append(src).append(" ").append(dst);
            return new SshCommand(commandBuilder.toString());
        }

        public SshCommand moveFile(String src, String dst, boolean force) {
            List<String> options = Lists.newArrayList();
            if (force)
                options.add("f");
            StringBuilder commandBuilder = new StringBuilder().append("mv");
            if (options.size() > 0)
                commandBuilder.append(" ").append("-").append(StringUtils.join(options, ""));
            commandBuilder.append(" ").append(src).append(" ").append(dst);
            return new SshCommand(commandBuilder.toString());
        }

        public SshCommand deleteFile(String path, boolean force, boolean recursive) {
            List<String> options = Lists.newArrayList();
            if (force)
                options.add("f");
            if (recursive)
                options.add("R");
            StringBuilder commandBuilder = new StringBuilder().append("rm");
            if (options.size() > 0)
                commandBuilder.append(" ").append("-").append(StringUtils.join(options, ""));
            commandBuilder.append(" ").append(path);
            return new SshCommand(commandBuilder.toString());
        }
    }

}
