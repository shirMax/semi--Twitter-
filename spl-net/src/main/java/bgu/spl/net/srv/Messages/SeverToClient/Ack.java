package bgu.spl.net.srv.Messages.SeverToClient;

import bgu.spl.net.srv.Messages.Message;

public class Ack extends Message {
    private short messageOpCode;
    private String optional = "";

    public Ack(short messageOpCode, String optional) {
        opCode = 10;
        this.messageOpCode = messageOpCode;
        this.optional = optional;

    }

    public short getMessageOpCode() {
        return messageOpCode;
    }

    public String getOptional() {
        return optional;
    }

    @Override
    public String toString() {
        return "ACK " + messageOpCode + " " + optional;
    }
}
