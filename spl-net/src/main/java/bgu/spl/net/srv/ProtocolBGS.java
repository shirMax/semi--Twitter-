package bgu.spl.net.srv;

import bgu.spl.net.BidiMessagingProtocol;
import bgu.spl.net.srv.Connections.Connections;
import bgu.spl.net.srv.Messages.ClientToServer.*;
import bgu.spl.net.srv.Messages.Message;
import bgu.spl.net.srv.Messages.SeverToClient.*;
import bgu.spl.net.srv.Messages.SeverToClient.Error;

public class ProtocolBGS<T> implements BidiMessagingProtocol<T> {
    private Cluster cluster;
    boolean shouldTerminate = false;

    private int connectionId;
    private String username;
    private Connections connectionsImp;

    public ProtocolBGS(Cluster cluster) {
        this.cluster = cluster;
        username = null;
    }

    @Override
    public void start(int connectionId, Connections connectionsImp) {
        this.connectionsImp = connectionsImp;
        this.connectionId = connectionId;
    }

    @Override
    public void process(T message) {
        short opCode = ((Message)message).getOpCode();
        boolean succeeded;
        switch (opCode) {
            case 1: { //register
                Register reg = (Register) message;
                if (cluster.register(reg.getUsername(), reg.getPassword(), reg.getBirthday()))
                    connectionsImp.send(connectionId, new Ack(reg.getOpCode(), ""));
                else
                    connectionsImp.send(connectionId, new Error(opCode));
                break;
            }
            case 2: { //login
                LogIn login = (LogIn) message;
                if (cluster.login(login.getUserName(), login.getPassword(), login.isCaptcha(), connectionId))
                    username = login.getUserName();
                else
                    connectionsImp.send(connectionId, new Error(opCode));
                break;
            }
            case 3: { //logout
                LogOut logout = (LogOut) message;
                if (username != null && cluster.logout(username)) {
                    username = null;
                    boolean response = connectionsImp.send(connectionId, new Ack(logout.getOpCode(), ""));
                    if (response)
                        connectionsImp.disconnect(connectionId);
                } else
                    connectionsImp.send(connectionId, new Error(opCode));
                break;
            }
            case 4: { //follow / unfollow
                FollowUnfollow followUnfollow = (FollowUnfollow) message;
                if (cluster.follow(username, followUnfollow.getUserName(), followUnfollow.isFollow()))
                    connectionsImp.send(connectionId, new Ack(followUnfollow.getOpCode(), followUnfollow.getUserName()));
                else
                    connectionsImp.send(connectionId, new Error(opCode));
                break;
            }
            case 5: { //post
                Post post = (Post) message;
                if (cluster.post(username, post, post.getContent())) {
                    connectionsImp.send(connectionId, new Ack(post.getOpCode(), ""));
                    for (String recipient : post.getTagList())
                        connectionsImp.send(cluster.getUsersMap().get(recipient).getConnectionId(), new Notification(true, username, post.getContent()));
                    for (User recipient : cluster.getUsersMap().get(username).getFollowersMap().values())
                        connectionsImp.send(recipient.getConnectionId(), new Notification(true, username, post.getContent()));
                } else
                    connectionsImp.send(connectionId, new Error(opCode));
                break;
            }
            case 6: {//PM
                PM pm = (PM) message;
                if (cluster.PM(username, pm))
                    connectionsImp.send(cluster.getUsersMap().get(pm.getUsername()).getConnectionId(), new Notification(false, username, pm.getContent()+" "+pm.getSendingDT()));
                else
                    connectionsImp.send(connectionId, new Error(opCode));
                break;
            }
            case 7: { //logStat
                LogStat logStat = (LogStat) message;
                if (cluster.logStat(username, logStat))
                    for (String log : logStat.getUserList())
                        connectionsImp.send(connectionId, new Ack(logStat.getOpCode(), log));
                else
                    connectionsImp.send(connectionId, new Error(opCode));
                break;
            }
            case 8: { //stat
                Stat stat = (Stat) message;
                if (cluster.stat(username, stat.getUsernames(), stat))
                    for (String log : stat.getLogs())
                        connectionsImp.send(connectionId, new Ack(stat.getOpCode(), log));
                else
                    connectionsImp.send(connectionId, new Error(opCode));
                break;
            }
            case 12: { //block
                Block block = (Block) message;
                succeeded = cluster.block(username, block.getUserName());
                if (!succeeded)
                    connectionsImp.send(connectionId, new Error(opCode));
                break;
            }
        }
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
