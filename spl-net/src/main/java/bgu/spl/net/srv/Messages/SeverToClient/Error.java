package bgu.spl.net.srv.Messages.SeverToClient;

import bgu.spl.net.srv.Messages.Message;

public class Error extends Message {
    private final short messageOpcode;

    public short getMessageOpcode() {
        return messageOpcode;
    }

    public Error(short messageOpcode) {
        opCode = 11;
        this.messageOpcode = messageOpcode;
    }

    @Override
    public String toString() {
        return "ERROR " + messageOpcode;
    }


}
