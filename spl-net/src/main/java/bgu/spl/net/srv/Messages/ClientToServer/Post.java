package bgu.spl.net.srv.Messages.ClientToServer;

import bgu.spl.net.srv.Messages.Message;

import java.util.List;
import java.util.LinkedList;

public class Post extends Message {
    private final String content;
    private List<String> tagList;

    public Post(String content) {
        opCode = 5;
        this.content = content;
        tagList = new LinkedList<>();
    }

    public Post(String content, List<String> tagList) {
        opCode = 5;
        this.content = content;
        this.tagList = tagList;
    }

    public String getContent() {
        return content;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    @Override
    public String toString() {
        return opCode + content + "0";
    }
}
