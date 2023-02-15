package bgu.spl.net.srv.Messages.ClientToServer;

import bgu.spl.net.srv.Messages.Message;

public class PM extends Message {
    private String username;
    private String content;
    private String sendingDT;

    public PM(String username, String content, String sendingDT) {
        opCode=6;
        this.username = username;
        this.content = content;
        this.sendingDT = sendingDT;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public String getSendingDT() {
        return sendingDT;
    }

    @Override
    public String toString() {
        return opCode + username + "0" + content + "0" + sendingDT + "0";
    }
}
