package bgu.spl.net.srv;

import java.util.*;
import java.time.LocalDate;

public class User {
    private String username;
    private String password;
    private LocalDate birthday;
    private boolean isLoggedIn;

    private int connectionId;
    private Map<String, User> followingMap;
    private Map<String, User> followersMap;
    private Map<String, User> blockMap;

    public User(String username, String password, LocalDate birthday) {
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.isLoggedIn = false;
        connectionId=-1;
        this.followingMap = new HashMap<>();
        this.followersMap = new HashMap<>();
        blockMap = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn, int connectionId) {
        isLoggedIn = loggedIn;
        this.connectionId = connectionId;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public Map<String, User> getFollowingMap() {
        return followingMap;
    }

    public void setFollowingMap(Map<String, User> followingMap) {
        this.followingMap = followingMap;
    }

    public Map<String, User> getFollowersMap() {
        return followersMap;
    }

    public void setFollowersMap(Map<String, User> followersMap) {
        this.followersMap = followersMap;
    }

    public Map<String, User> getBlockMap() {
        return blockMap;
    }

    public void setBlockMap(Map<String, User> blockMap) {
        this.blockMap = blockMap;
    }


    public synchronized boolean validate_adding_following(String username) {
        return !followingMap.containsKey(username);
    }

    public synchronized boolean validate_adding_followers(String username) {
        return !followersMap.containsKey(username);
    }

    public synchronized boolean validate_removing_following(String username) {
        return followingMap.containsKey(username);
    }

    public synchronized boolean validate_removing_followers(String username) {
        return followersMap.containsKey(username);
    }

    public synchronized void addFollowingMap(String username, User user) {
        if (validate_adding_following(username))
            followingMap.put(username, user);
    }

    public synchronized void removeFollowersMap(String username) {
        if (validate_removing_followers(username))
            followersMap.remove(username);
    }

    public synchronized void removeFollowing(String username) {
        if (validate_removing_following(username))
            followingMap.remove(username);
    }

    public synchronized void addFollowersMap(String username, User user) {
        if (validate_adding_followers(username))
            followersMap.put(username, user);
    }

    public synchronized boolean addBlock(String username, User user) {
        if (blockMap.containsKey(username))
            return false;
        blockMap.put(username, user);
        removeFollowing(username);
        removeFollowersMap(username);
        return true;
    }
}
