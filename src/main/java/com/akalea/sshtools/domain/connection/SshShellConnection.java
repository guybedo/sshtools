package com.akalea.sshtools.domain.connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.lang3.StringUtils;

import com.akalea.sshtools.domain.command.SshCommand;
import com.akalea.sshtools.domain.command.SshCommandExecution;
import com.akalea.sshtools.domain.session.SshConnectionType;
import com.google.common.collect.Lists;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.Session;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SshShellConnection extends SshConnection {

    public SshShellConnection(Session session) {
        super(session, SshConnectionType.shell);
    }

    public SshCommandExecution executeCommand(SshCommand command) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            getChannel().setOutputStream(out);
            getChannel().setInputStream(
                new ReaderInputStream(
                    new StringReader(String.format("%s\n", command.getCommand())),
                    Charset.defaultCharset()));
            getChannel().connect();
            while (out.size() == 0)
                Thread.currentThread().sleep(100);
            SshCommandExecution execution = result(command, out);
            getChannel().disconnect();
            return execution;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<SshCommandExecution> executeCommands(
        List<SshCommand> commands,
        boolean failOnError) {
        List<SshCommandExecution> executions = Lists.newArrayList();
        String script =
            StringUtils
                .join(
                    commands.stream()
                        .map(command -> command.getCommand())
                        .collect(Collectors.toList()),
                    " && ");
        SshCommand scriptCommand = new SshCommand<>(script);

        try {
            openChannel(false);
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream commandsInput = new PipedOutputStream(in);
            PrintStream ps =
                new PrintStream(
                    new ByteArrayOutputStream(),
                    true,
                    "UTF-8") {
                    @Override
                    public void write(byte[] b, int off, int len) {
                        byte[] bytes =
                            new String(b, off, len)
                                .replaceAll("\u001B\\[[;\\d]*m", "")
                                .getBytes();
                        try {
                            bOut.write(bytes);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
            getChannel().setInputStream(in);
            getChannel().setOutputStream(ps);
            getChannel().connect();
            commandsInput.write(script.getBytes());
            commandsInput.write("exit\n".getBytes());
            while (!getChannel().isClosed())
                Thread.currentThread().sleep(100);
            getChannel().disconnect();

            executions.add(new SshCommandExecution(scriptCommand).setResult(toLines(bOut), null));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return executions;
    }

    private SshCommandExecution result(
        SshCommand command,
        ByteArrayOutputStream out)
        throws IOException {
        List<String> stdouts = toLines(out);
        Object result =
            Optional
                .ofNullable(command.getResultExtractor())
                .map(extractor -> extractor.apply(stdouts))
                .orElse(null);
        SshCommandExecution execution = new SshCommandExecution(command);
        execution.setResult(stdouts, null, result);
        return execution;
    }

    private ChannelShell getChannel() {
        return (ChannelShell) this.channel;
    }

}
