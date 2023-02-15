package bgu.spl.net.srv;

import bgu.spl.net.srv.Messages.Message;

public class TPCMain {
    public static void main(String[] args) {
        Cluster cluster = new Cluster();
        try (BaseServer<Message> server = new TPCServer(
                7777,
                () -> new ProtocolBGS(cluster),
                () -> new MsgEncoderDecoder())) {
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
