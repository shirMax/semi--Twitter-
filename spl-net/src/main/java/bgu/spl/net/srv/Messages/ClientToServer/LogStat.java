package bgu.spl.net.srv.Messages.ClientToServer;

import bgu.spl.net.srv.Messages.Message;

import java.util.List;

public class LogStat extends Message {
    private List<String> userList;

    public LogStat() {
        opCode = 7;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }

}
