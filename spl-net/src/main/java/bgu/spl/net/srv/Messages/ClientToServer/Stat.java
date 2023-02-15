package bgu.spl.net.srv.Messages.ClientToServer;

import bgu.spl.net.srv.Messages.Message;

import java.util.List;

public class Stat extends Message {
    List<String> usernames;
    List<String> logs;

    public Stat(List<String> usernames) {
        opCode = 8;
        this.usernames = usernames;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

    @Override
    public String toString() {
        return opCode + String.join("\\|", usernames);
    }
}
