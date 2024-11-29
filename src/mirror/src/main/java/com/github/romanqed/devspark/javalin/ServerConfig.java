package com.github.romanqed.devspark.javalin;

public final class ServerConfig {
    private String host;
    private int port;
    private String login;
    private String password;
    private boolean enableCli;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnableCli() {
        return enableCli;
    }

    public void setEnableCli(boolean enableCli) {
        this.enableCli = enableCli;
    }
}
