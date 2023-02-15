package bgu.spl.net.srv;

import bgu.spl.net.BidiMessagingProtocol;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.srv.Connections.BlockingConnectionHandler;
import bgu.spl.net.srv.Connections.ConnectionsImp;

import java.util.function.Supplier;

public class TPCServer<T> extends BaseServer<T> {
    private ConnectionsImp connectionsImp;

    public TPCServer(int port, Supplier<BidiMessagingProtocol<T>> protocolFactory, Supplier<MessageEncoderDecoder<T>> encdecFactory) {
        super(port, protocolFactory, encdecFactory);
        connectionsImp = new ConnectionsImp();
    }


    @Override
    protected void execute(BlockingConnectionHandler<T> handler) {
        connectionsImp.addConnection(handler);
        new Thread(handler).start();
    }
}
