package com.abc.xyz.os10.models;

/**
 * Created by Admin on 4/12/2016.
 */
public class Message {
    private String name, content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Message(String name, String content) {

        this.name = name;
        this.content = content;
    }

    public Message() {

    }
}
