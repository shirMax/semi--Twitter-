package bgu.spl.net.srv.Messages.ClientToServer;

import bgu.spl.net.srv.Messages.Message;

public class Block extends Message {
    private String userName;

    public String getUserName() {
        return userName;
    }

    public Block(String userName) {
        opCode=12;
        this.userName = userName;
    }

    @Override
    public String toString() {
        return null;
    }
}
