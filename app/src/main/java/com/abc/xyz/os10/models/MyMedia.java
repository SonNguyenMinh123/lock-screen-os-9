package com.abc.xyz.os10.models;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

/**
 * Created by Admin on 06/11/2015.
 */
public class MyMedia {
    private final String TAG = "MyMedia";
    private static final int STATE_STOP = -1;
    private static final int STATE_PLAYING = 0;
    private static final int STATE_PAUSE = 1;
    private int playState = STATE_STOP;
    MediaPlayer mediaPlayer = new MediaPlayer();
    private Context mContext;
    private int currentIndex = 0, currentLength = 0;
    private String path;
    public MyMedia(Context context) {
        this.mContext = context;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void play() {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(mContext, Uri.parse(path));
            mediaPlayer.prepare();
            playState = STATE_PLAYING;
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public void next() {
//        if (currentIndex >= arrayMusic.size() - 1) {
//            currentIndex = 0;
//        } else {
//            currentIndex++;
//        }
//        play();
    }

    public void previous() {
//        if (currentIndex <= 0) {
//            currentIndex = arrayMusic.size() - 1;
//        } else {
//            currentIndex--;
//        }
//        play();
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            currentLength = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            playState = STATE_PAUSE;
        }
    }

    public void resume() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(currentLength);
            mediaPlayer.start();
            playState = STATE_PLAYING;
        }
    }

    public void stop() {
        //mPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
    }

    public void seek(int seekTo) {
        mediaPlayer.seekTo(seekTo);
        currentLength = mediaPlayer.getCurrentPosition();
//        mediaPlayer.start();
    }



}
