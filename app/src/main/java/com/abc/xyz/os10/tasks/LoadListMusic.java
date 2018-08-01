package com.abc.xyz.os10.tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.abc.xyz.os10.models.ItemMusic;

import java.util.ArrayList;

/**
 * Created by Admin on 7/6/2016.
 */
public class LoadListMusic extends AsyncTask<String, String, String> {
    private Context mContext;
    private OnLoadListMusicListener mLoadListMusicListener;
    private ArrayList<ItemMusic> arrayMusic = new ArrayList<>();
    public LoadListMusic(Context mContext, OnLoadListMusicListener loadListMusicListener) {
        this.mContext = mContext;
        mLoadListMusicListener = loadListMusicListener;
    }


    @Override
    protected String doInBackground(String... params) {
        initArrMusic();
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(mLoadListMusicListener != null){
            mLoadListMusicListener.onPostExcute(arrayMusic);
        }
    }

    public void initArrMusic() {
        String columnsName[] = new String[]{MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.AudioColumns.DISPLAY_NAME,
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.ARTIST,
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.Albums.ALBUM_ID};
        @SuppressLint({"Recycle", "NewApi", "LocalSuppress"})
        Cursor c = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columnsName, null, null, null, null);
        c.moveToFirst();
        arrayMusic.clear();
        int pathIndex = c.getColumnIndex(columnsName[0]);
        int fullNameIndex = c.getColumnIndex(columnsName[1]);
        int durationIndex = c.getColumnIndex(columnsName[2]);
        int authorIndex = c.getColumnIndex(columnsName[3]);
        int songNameIndex = c.getColumnIndex(columnsName[4]);
        int albumID = c.getColumnIndex(columnsName[5]);
        while (c.isAfterLast() == false) {
            Log.e("initArrMusic", "initArrMusic: "+c.getInt(durationIndex) );
            if(c.getInt(durationIndex)<10000){
                Log.e("initArrMusic", "initArrMusic: "+c.getInt(durationIndex) );
                c.moveToNext();
                continue;
            }
            arrayMusic.add(new ItemMusic(c.getString(songNameIndex),
                    c.getString(pathIndex),
                    c.getString(durationIndex),
                    c.getString(authorIndex),
                    c.getString(fullNameIndex),
                    c.getInt(albumID),
                    getImgSong(c.getString(pathIndex))));
            c.moveToNext();
        }
    }
    public byte[] getImgSong(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        byte[] byteArray = mmr.getEmbeddedPicture();
        if (byteArray != null) {
            return byteArray;
        } else {
            return null;
        }
    }
    public interface OnLoadListMusicListener {
        public void onPostExcute(ArrayList<ItemMusic> arrItemMusic);
    }
}
