package com.abc.xyz.services.lockscreenios9;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.abc.xyz.entities.SongEntity;
import com.abc.xyz.os10.controllers.PagerControlMusic;
import com.abc.xyz.utils.MyToast;
import com.abc.xyz.utils.SharedPreferencesUtil;
import com.abc.xyz.utils.logs.LogUtil;
import com.abc.xyz.views.partials.UnlockLayout;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by DucNguyen on 24/02/2016.
 */
public class MusicPlayerService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {
    private static final String TAG = "MusicPlayerService";
    //media player
    public MediaPlayer player;
    //song list
    private ArrayList<SongEntity> mSongEntityArrayList;
    //current position
    private int songPosn;
    //binder
    private final IBinder musicBind = new MusicBinder();
    //title of current song
    private String songTitle = "";
    private String songArtist = "";
    //notification id
    private static final int NOTIFY_ID = 1;
    //shuffle flag and random
    private boolean shuffle = false;
    private Random rand;

    public void onCreate() {
        //create the service
        super.onCreate();
        //initialize position
        songPosn = 0;
        //random
        rand = new Random();
        //create player
        player = new MediaPlayer();
        //initialize
        initMusicPlayer();
    }

    public void initMusicPlayer() {
        //set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //set listeners
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    //pass song list
    public void setList(ArrayList<SongEntity> theSongs) {
        mSongEntityArrayList = theSongs;
    }

    //binder
    public class MusicBinder extends Binder {
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }

    //activity will bind to service
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    //release resources when unbind
    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    //play a song
    public void playSong() {
        if(mSongEntityArrayList !=null && mSongEntityArrayList.size()==0) {
            MyToast.showToast(this, "No fopund!");
            return;
        }
//        //play
        try {
            player.reset();
        } catch (Exception e) {
            try {
                if (player.isPlaying()) {
                    player.stop();
                }
            } catch (IllegalStateException e1) {
                e1.printStackTrace();
            }

            player = new MediaPlayer();
            //initialize
            initMusicPlayer();
        }
        //get song
        SongEntity playSong = mSongEntityArrayList.get(songPosn);
        //get title
        songTitle = playSong.getTitle();
        songArtist = playSong.getArtist();
        //get id
        long currSong = playSong.getID();
        //set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);
        //set the data source
        try {
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        try {
            player.prepareAsync();
        } catch (IllegalStateException e) {
            try {
                player.start();
            } catch (IllegalStateException e1) {
                e1.printStackTrace();
            }
        }
    }

    public String getSongTitle() {
        return songTitle;
    }

    public String getArtistName() {
        return songArtist;
    }

    //set the song
    public void setSong(int songIndex) {
        songPosn = songIndex;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //check if playback has reached the end of a track
        LogUtil.getLogger().d(TAG, "onCompletion");

        if ((!PagerControlMusic.firstrun || !UnlockLayout.firstrun) && player.getCurrentPosition() > 0) {
            mp.reset();
            if (SharedPreferencesUtil.isRepeatMusic(this)) {
                playSong();
            } else {
                playNext();
            }

        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.v("MUSIC PLAYER", "Playback Error");
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();
    }

    public void stopMusic() {
        player.stop();
    }

    //playback methods
    public int getPosn() {
        return player.getCurrentPosition();
    }

    public int getDur() {
        return player.getDuration();
    }

    public boolean isPng() {
        return player.isPlaying();
    }

    public void pausePlayer() {
        try {
            player.pause();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void seek(int posn) {
        player.seekTo(posn);
    }

    public void go() {
        player.start();
    }

    //skip to previous track
    public void playPrev() {
        songPosn--;
        if (songPosn < 0) songPosn = mSongEntityArrayList.size() - 1;
        playSong();
    }

    //skip to next
    public void playNext() {
        if (shuffle) {
            int newSong = songPosn;
            while (newSong == songPosn) {
                newSong = rand.nextInt(mSongEntityArrayList.size());
            }
            songPosn = newSong;
        } else {
            songPosn++;
            if (songPosn >= mSongEntityArrayList.size()) songPosn = 0;
        }
        playSong();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    //toggle shuffle
    public void setShuffle() {
        if (shuffle) shuffle = false;
        else shuffle = true;
    }
}
