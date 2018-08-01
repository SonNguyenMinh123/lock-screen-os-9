package com.abc.xyz.os10.models.sms;

/**
 * Created by Admin on 7/5/2016.
 */
public class ItemSmS {
    private String body;
    private String address;
    private String time;
    private int type = 1;
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ItemSmS() {

    }

    public ItemSmS(String body, String address, String time, int type) {
        this.body = body;
        this.address = address;
        this.time = time;
        this.type = type;
    }
}
