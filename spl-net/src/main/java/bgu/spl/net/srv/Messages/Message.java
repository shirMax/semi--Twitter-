package bgu.spl.net.srv.Messages;

public abstract class Message {
    protected short opCode;

    public short getOpCode() {
        return opCode;
    }
}
