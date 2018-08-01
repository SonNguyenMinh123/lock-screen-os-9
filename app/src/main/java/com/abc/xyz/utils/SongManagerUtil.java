package com.abc.xyz.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.abc.xyz.entities.SongEntity;

import java.util.ArrayList;

/**
 * Created by DucNguyen on 24/02/2016.
 */
public class SongManagerUtil {
    //method to retrieve song info from device
    public static ArrayList<SongEntity> getSongList(Context context) {
        ArrayList<SongEntity> songEntityArrayList = new ArrayList<>();
        //query external audio
        ContentResolver musicResolver = context.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        //iterate over results if valid
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songEntityArrayList.add(new SongEntity(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
        return songEntityArrayList;
    }

}
