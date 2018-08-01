package com.screen.videos.objects;

public class Videos {
    private String nameVideo;
    private String filePath;

    public Videos(String nameVideo, String filePath) {
        this.nameVideo = nameVideo;
        this.filePath = filePath;
    }

    public String getNameVideo() {
        return nameVideo;
    }

    public String getFilePath() {
        return filePath;
    }
}
