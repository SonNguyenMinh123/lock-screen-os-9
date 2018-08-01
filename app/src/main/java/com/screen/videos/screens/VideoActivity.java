package com.screen.videos.screens;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ListView;

import com.screen.videos.R;
import com.screen.videos.adapter.AdapterListVideo;
import com.screen.videos.objects.Videos;
import com.screen.videos.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

import cn.pedant.sweetalert.SweetAlertDialog;

public class VideoActivity extends Activity{
    private ArrayList<Videos> videosArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_video);
        try {
            String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/" + "abc" + "/";
            File file = new File(rootDir);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                }
            }
            File[] fileList = file.listFiles();
            videosArrayList = new ArrayList<>();
            for (int i = 0 ; i < fileList.length;i++){
                if (fileList[i].getName().contains(".mp4"))
                videosArrayList.add(new Videos(fileList[i].getName(), fileList[i].getPath()));
            }

            final AdapterListVideo adapterListVideo = new AdapterListVideo(this, videosArrayList);
            adapterListVideo.listenerAdapter(new AdapterListVideo.CommucationAdapter() {
                @Override
                public void onClickDeleteBtn(final String pathFile, final int position) {
                    new SweetAlertDialog(VideoActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("Won't be able to recover this file!")
                            .setConfirmText("Yes,delete it!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Log.e("test","click ");
                                    File file = new File(pathFile);
                                    if (file.exists())
                                        file.delete();
                                    FileUtils.notifyMediaScannerService(VideoActivity.this, file.getAbsolutePath());
                                    videosArrayList.remove(position);
                                    adapterListVideo.notifyDataSetChanged();
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();
                }
            });
            ListView lvVideoRecorder = (ListView) findViewById(R.id.lv_list_video);
            lvVideoRecorder.setAdapter(adapterListVideo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
