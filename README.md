sshtools
--------

Java SSH tools - easier SSH &amp; SFTP in Java

Sshtools is a wrapper around http://www.jcraft.com/jsch/. 

The goal is to make it easier to do SSH & SFTP in Java, as using Jsch can be tricky.

Getting Started
---------------

Maven
-----

```xml
<dependency>
    <groupId>com.akalea</groupId>
    <artifactId>ssh-tools</artifactId>
    <version>0.2.0</version>
</dependency>
```

Usage
-----

**Setup your SSH connection**:

```java
SshServerInfo serverInfo =
    new SshServerInfo(
        "login",
        "localhost",
        "/home/user/.ssh/id_rsa",
        null);
```

**Delete a file**:

```java
Ssh
	.of(serverInfo)
	.file()
	.deleteFile("/home/user/test.txt");
```

**Execute shell commands**:

```java 
Ssh
    .of(serverInfo)
    .command()
    .execute(commands);
```

**SFTP**:

```java 
Ssh
	.of(serverInfo)
	.sftp()
	.execute(
	    Lists.newArrayList(
	        SftpCommand.put(localFile, remoteFile),
	        SftpCommand.get(remoteFile, downloadedFile)));
```

**OTHER EXAMPLES**:

You can find examples here: [Examples](http://github.com/guybedo/sshtools/tree/master/src/main/java/com/akalea/sshtools/example)
