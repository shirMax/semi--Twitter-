package bgu.spl.net.srv.Messages.ClientToServer;

import bgu.spl.net.srv.Messages.Message;

public class LogOut extends Message {

    public LogOut() {
        opCode = 3;
    }
}
