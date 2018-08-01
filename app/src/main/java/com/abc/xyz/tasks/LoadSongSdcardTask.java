package com.abc.xyz.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.abc.xyz.entities.SongEntity;
import com.abc.xyz.utils.SongManagerUtil;

import java.util.ArrayList;

/**
 * Created by DucNguyen on 25/02/2016.
 */
public class LoadSongSdcardTask extends AsyncTask<Void, Void, ArrayList<SongEntity>> {
    private Context mcontext;
    private CommunicatorLoadLocation mCommunicatorLoadLocation;

    public LoadSongSdcardTask(Context mcontext, CommunicatorLoadLocation communicatorLoadLocation) {
        this.mcontext = mcontext;
        mCommunicatorLoadLocation = communicatorLoadLocation;
    }

    @Override
    protected ArrayList<SongEntity> doInBackground(Void... params) {
        return SongManagerUtil.getSongList(mcontext);
    }

    @Override
    protected void onPostExecute(ArrayList<SongEntity> songEntities) {
        super.onPostExecute(songEntities);
        if (mCommunicatorLoadLocation!=null)
        mCommunicatorLoadLocation.onPostExecute(songEntities);
    }

    public interface CommunicatorLoadLocation {
        public void onPostExecute(ArrayList<SongEntity> songEntityArrayList);

    }
}
