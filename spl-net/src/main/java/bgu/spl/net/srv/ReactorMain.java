package bgu.spl.net.srv;

import bgu.spl.net.srv.Messages.Message;

import java.util.Scanner;

public class ReactorMain {
    public static void main(String[] args) {
        Cluster cluster = new Cluster();
        Scanner scanner = new Scanner(System.in);
        int numofthreds = scanner.nextInt();
        try (Server<Message> server = new Reactor<Message>(
                numofthreds, 7777,
                () -> new ProtocolBGS(cluster),
                () -> new MsgEncoderDecoder())) {
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
