package bgu.spl.net.srv.Connections;

import bgu.spl.net.srv.Messages.Message;

import java.util.concurrent.ConcurrentHashMap;


public class ConnectionsImp<T> implements Connections<T> {
    private static int connectionId = 0;
    ConcurrentHashMap<Integer, ConnectionHandler<T>> connections;

    public ConnectionsImp() {
        this.connections = new ConcurrentHashMap<>();
    }

    @Override
    public boolean send(int connectionId, T msg) {
        if (!connections.containsKey(connectionId))
            return false;
        if(msg!=null)
            connections.get(connectionId).send(msg);
        return true;
    }

    @Override
    public void broadcast(T msg) {
        for (ConnectionHandler<T> connectionHandler : connections.values()) {
            connectionHandler.send(msg);
        }
    }

    @Override
    public void disconnect(int connectionId) {
        connections.remove(connectionId);
    }

    public void addConnection(ConnectionHandler<T> connectionHandler) {
        connections.put(connectionId, connectionHandler);
        int id = connectionId;
        connectionHandler.initiate(id, this);
        connectionId++;
    }
}
