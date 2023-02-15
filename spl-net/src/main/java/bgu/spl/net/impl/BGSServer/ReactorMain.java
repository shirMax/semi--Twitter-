package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.srv.*;
import bgu.spl.net.srv.Messages.Message;

import java.util.Scanner;

public class ReactorMain {
    public static void main(String[] args) {
        Cluster cluster = new Cluster();
        try (Server<Message> server = new Reactor<Message>(
                Integer.parseInt(args[1]), Integer.parseInt(args[0]),
                () -> new ProtocolBGS(cluster),
                () -> new MsgEncoderDecoder())) {
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
