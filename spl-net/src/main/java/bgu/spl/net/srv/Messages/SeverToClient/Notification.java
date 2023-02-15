package bgu.spl.net.srv.Messages.SeverToClient;

import bgu.spl.net.srv.Messages.Message;

public class Notification extends Message {
    private final boolean notificationType;
    private final String postingUser;
    private final String content;

    public Notification(boolean notificationType, String postingUser, String content) {
        opCode = 9;
        this.notificationType = notificationType;
        this.postingUser = postingUser;
        this.content = content;
    }

    public boolean isNotificationType() {
        return notificationType;
    }

    public String getPostingUser() {
        return postingUser;
    }

    public String getContent() {
        return content;
    }

    public String toString() {
        return "NOTIFICATION " + (notificationType ? "PM" : "Public") + " " + postingUser + " " + content;
    }
}
