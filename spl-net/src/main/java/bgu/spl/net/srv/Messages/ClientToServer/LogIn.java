package bgu.spl.net.srv.Messages.ClientToServer;

import bgu.spl.net.srv.Messages.Message;

public class LogIn extends Message {
    private String userName;
    private String password;
    private boolean captcha;

    public LogIn(String userName, String password, boolean captcha) {
        opCode = 2;
        this.userName = userName;
        this.password = password;
        this.captcha = captcha;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isCaptcha() {
        return captcha;
    }

    @Override
    public String toString() {
        return opCode + userName + "0" + password + "0" + (captcha ? 1 : 0);
    }
}
