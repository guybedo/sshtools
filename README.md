# sshtools
Java SSH tools - easier SSH &amp; SFTP in Java

Sshtools is a wrapper around http://www.jcraft.com/jsch/. The goal is to make it easier to do SSH & SFTP in Java, as using Jsch can be a little tricky.

# usage

### Setup your SSH connection:
```java
SshServerInfo serverInfo =
    new SshServerInfo(
        "login",
        "localhost",
        "/home/user/.ssh/id_rsa",
        null);
```

### Delete a file:
```java
SshProcesses
    .files()
    .deleteFile(
        serverInfo,
        "/home/user/test.txt",
        true,
        true);
```

### Execute shell commands:
```java 
List<SshCommand> commands =
    Lists.newArrayList(
        new SshCommand("java -version"),
        new SshCommand("cd /data"),
        new SshCommand("ls -l"));
SshService
    .shell(serverInfo, commands, false)
```

You can find examples here: [Examples](http://github.com/guybedo/sshtools/tree/master/src/main/java/com/akalea/sshtools/example)
