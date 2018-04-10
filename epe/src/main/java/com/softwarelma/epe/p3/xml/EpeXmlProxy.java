package com.softwarelma.epe.p3.xml;

public class EpeXmlProxy {

    private String host;
    private String port;
    private String username;
    private String password;

    public EpeXmlProxy(String host, String port, String username, String password) {
        super();
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "EpeXmlProxy [host=" + host + ", port=" + port + ", username=" + username + ", password=" + password
                + "]";
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
