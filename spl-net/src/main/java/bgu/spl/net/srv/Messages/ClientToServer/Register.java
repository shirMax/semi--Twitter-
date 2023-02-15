package bgu.spl.net.srv.Messages.ClientToServer;


import bgu.spl.net.srv.Messages.Message;

public class Register extends Message {
    private final String username;
    private final String password;
    private final String birthday;

    public Register(String username, String password, String birthday) {
        opCode=1;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }

    @Override
    public String toString() {
        return opCode + username + "0" + password + "0" + birthday + "0";
    }
}
