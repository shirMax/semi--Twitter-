package bgu.spl.net.srv.Messages.ClientToServer;

import bgu.spl.net.srv.Messages.Message;

public class FollowUnfollow extends Message {
    private boolean isFollow; // 0 follow 1 UNfollow
    private String userName;

    public FollowUnfollow(boolean isFollow, String userName) {
        opCode = 4;
        this.isFollow = isFollow;
        this.userName = userName;
    }

    public boolean isFollow() {
        return !isFollow;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return opCode + (isFollow ? 1 : 0) + userName;
    }

}
