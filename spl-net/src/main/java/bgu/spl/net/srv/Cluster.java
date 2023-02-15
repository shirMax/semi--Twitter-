package bgu.spl.net.srv;

import bgu.spl.net.srv.Messages.ClientToServer.LogStat;
import bgu.spl.net.srv.Messages.ClientToServer.Stat;
import bgu.spl.net.srv.Messages.ClientToServer.PM;
import bgu.spl.net.srv.Messages.ClientToServer.Post;

import java.time.Period;
import java.util.*;
import java.time.LocalDate;

public class Cluster {

    private final Map<String, User> usersMap;
    private final Map<String, List<PM>> pmMap;
    private final Map<String, List<Post>> postsMap;
    private final List<String> filtered_words;

    public Cluster() {
        usersMap = new HashMap<>();
        pmMap = new HashMap<>();
        postsMap = new HashMap<>();
        filtered_words = new LinkedList<>();
    }


    public boolean register(String username, String password, String birthday) {
        if (isRegistered(username)) //user is registered
            return false;
        LocalDate bDay = LocalDate.of(Integer.parseInt(birthday.substring(6)), Integer.parseInt(birthday.substring(3, 5)), Integer.parseInt(birthday.substring(0, 2)));
        usersMap.put(username, new User(username, password, bDay));
        postsMap.put(username, new LinkedList<>());
        pmMap.put(username, new LinkedList<>());
        return true;
    }

    public boolean login(String username, String password, boolean captcha, int connectionId) {
        if (!isRegistered(username) || usersMap.get(username).isLoggedIn() || !usersMap.get(username).getPassword().equals(password) || !captcha)
            return false;
        usersMap.get(username).setLoggedIn(true, connectionId);
        return true;
    }

    public boolean logout(String username) {
        if (!isRegistered(username) || !usersMap.get(username).isLoggedIn())
            return false;
        usersMap.get(username).setLoggedIn(false, -1);
        return true;
    }

    public boolean follow(String senderUser, String receiverUser, boolean followUnfollow) {
        if (!isRegistered(senderUser) || !isRegistered(receiverUser) || !usersMap.get(senderUser).isLoggedIn() || isBlocked(senderUser, receiverUser))
            return false;
        User receiver = usersMap.get(receiverUser);
        User sender = usersMap.get(senderUser);
        if (followUnfollow && (!(sender.validate_adding_following(receiverUser)) || !receiver.validate_adding_followers(senderUser)))
            return false;
        else if (!followUnfollow && (!sender.validate_removing_following(receiverUser) || !receiver.validate_removing_followers(senderUser)))
            return false;
        if (followUnfollow) {
            sender.addFollowingMap(receiverUser, receiver);
            receiver.addFollowersMap(senderUser, sender);
        } else {
            sender.removeFollowing(receiverUser);
            receiver.removeFollowersMap(senderUser);
        }
        return true;
    }

    public boolean post(String username, Post post, String content) {
        if (!isRegistered(username) || !usersMap.get(username).isLoggedIn())
            return false;
        List<String> taggedUsers = new LinkedList<>();
        for (int i = 0; i < content.length(); i++)
            if (content.charAt(i) == '@') {
                StringBuilder tagged = new StringBuilder();
                int j = i + 1;
                while (j < content.length() && content.charAt(j) != ' ')
                    tagged.append(content.charAt(j++));
                if (usersMap.containsKey(tagged.toString()))
                    taggedUsers.add(tagged.toString());
                i = j;
            }
        post.setTagList(taggedUsers);
        postsMap.get(username).add(new Post(content, taggedUsers));
        return true;
    }

    public boolean PM(String sender, PM pm) {
        if (!isRegistered(sender) || !usersMap.get(sender).isLoggedIn() || !isRegistered(pm.getUsername()) || !usersMap.get(sender).getFollowingMap().containsKey(pm.getUsername()) || isBlocked(sender, pm.getUsername()))
            return false;
        for (String word : filtered_words)
            pm.setContent(pm.getContent().replaceAll(word, "<filtered>"));
        pmMap.get(sender).add(pm);
        return true;
    }

    public boolean logStat(String username, LogStat logStat) {
        if (!isRegistered(username) || !usersMap.get(username).isLoggedIn())
            return false;
        List<String> logs = new LinkedList<>();
        for (User user : usersMap.values()) {
            boolean senderBlockedUser = usersMap.get(username).getBlockMap().containsKey(user.getUsername());
            boolean userBlockedSender = user.getBlockMap().containsKey(username);
            if (user.isLoggedIn() && !senderBlockedUser && !userBlockedSender)
                logUser(user, logs);
        }
        logStat.setUserList(logs);
        return true;
    }

    public boolean stat(String username, List<String> users, Stat stat) {
        if (!isRegistered(username) || !usersMap.get(username).isLoggedIn())
            return false;
        List<String> logs = new LinkedList<>();
        for (String user : users) {
            boolean senderBlockedUser = usersMap.get(user).getBlockMap().containsKey(username);
            boolean userBlockedSender = usersMap.get(username).getBlockMap().containsKey(user);
            if (!senderBlockedUser && !userBlockedSender)
                logUser(usersMap.get(user), logs);
            else
                return false;
        }
        stat.setLogs(logs);
        return true;
    }

    public boolean block(String sender, String receiver) {
        if (!isRegistered(sender) || !isRegistered(receiver) || !usersMap.get(sender).isLoggedIn() || usersMap.get(sender).addBlock(receiver, usersMap.get(receiver))) {
            usersMap.get(receiver).removeFollowing(sender);
            usersMap.get(receiver).removeFollowersMap(sender);
            return true;
        }
        return false;
    }

    public Map<String, User> getUsersMap() {
        return usersMap;
    }

    private boolean isRegistered(String username) {
        return usersMap.containsKey(username);
    }

    private boolean isBlocked(String sender, String recipient) {
        return usersMap.get(recipient).getBlockMap().containsKey(sender);
    }

    private void logUser(User u, List<String> logs) {
        String age = Integer.toString(Period.between(u.getBirthday(), LocalDate.now()).getYears());
        String posts = Integer.toString(postsMap.get(u.getUsername()).size());
        String followers = Integer.toString(u.getFollowersMap().size());
        String following = Integer.toString(u.getFollowingMap().size());
        logs.add(age + " " + posts + " " + followers + " " + following);
    }
}
