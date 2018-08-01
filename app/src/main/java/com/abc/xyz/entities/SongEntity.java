package com.abc.xyz.entities;

public class SongEntity {

    private long id;
    private String title;
    private String artist;

    public SongEntity(long songID, String songTitle, String songArtist) {
        id = songID;
        title = songTitle;
        artist = songArtist;
    }

    public long getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }
}
