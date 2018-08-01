package com.abc.xyz.os10.models;

import java.io.Serializable;

/**
 * Created by Admin on 05/11/2015.
 */
public class ItemMusic implements Serializable {
    private String songName, path, duration, author, fullName;
    private byte[] byteArray;
    private long albumID;

    public long getAlbumID() {
        return albumID;
    }

    public void setAlbumID(long albumID) {
        this.albumID = albumID;
    }

    public ItemMusic(String songName, String path, String duration, String author, String fullName,  long albumID, byte[] byteArray) {

        this.songName = songName;
        this.path = path;
        this.duration = duration;
        this.author = author;
        this.fullName = fullName;
        this.byteArray = byteArray;
        this.albumID = albumID;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }
}
