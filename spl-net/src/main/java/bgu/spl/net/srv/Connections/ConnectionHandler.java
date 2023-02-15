/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.net.srv.Connections;

import bgu.spl.net.srv.Messages.Message;

import java.io.Closeable;

/**
 * The ConnectionHandler interface for Message of type T
 */
public interface ConnectionHandler<Message> extends Closeable {
    public void send(Message message);

    void initiate(int id, ConnectionsImp connectionsImp);
}
