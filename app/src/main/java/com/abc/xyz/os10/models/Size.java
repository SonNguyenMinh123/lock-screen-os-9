package com.abc.xyz.os10.models;

import java.io.Serializable;

/**
 * Created by Admin on 18/06/2016.
 */
public class Size implements Serializable{
    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Size() {

    }

    public Size(int width, int height) {

        this.width = width;
        this.height = height;
    }
}
