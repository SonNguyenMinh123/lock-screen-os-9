package com.abc.xyz.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.abc.xyz.R;
import com.abc.xyz.adapters.WallpaperListAdapter;
import com.abc.xyz.configs.Constant;
import com.abc.xyz.datas.DataModel;


public class WallpaperListActivity extends AppCompatActivity implements OnItemClickListener {
    private GridView grvActivityWallpaper;
    private WallpaperListAdapter mWallpaperListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void initData() {
        mWallpaperListAdapter = new WallpaperListAdapter(this, DataModel.getWallpapers());
        grvActivityWallpaper.setAdapter(mWallpaperListAdapter);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        grvActivityWallpaper = (GridView) findViewById(R.id.grv_activity_wallpaper);
        grvActivityWallpaper.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, WallpaperSelectionActivity.class);
        intent.putExtra("position", position);
        startActivityForResult(intent, Constant.KEY_REQUEST_PIN_CANCEL);
        overridePendingTransition(R.anim.anim_fast_right_to_center, R.anim.anim_low_center_to_left);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        } else if (resultCode == RESULT_CANCELED) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_low_left_to_center, R.anim.anim_fast_center_to_right);
    }

}
