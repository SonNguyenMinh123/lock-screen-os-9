package com.screen.videos.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.screen.videos.objects.Videos;
import com.screen.videos.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AdapterListVideo extends ArrayAdapter {
    private ArrayList<Videos> arrVideo;
    private LayoutInflater inflater;
    private Context context;
    private CommucationAdapter commucationAdapter;

    public AdapterListVideo(Context context, ArrayList objects) {
        super(context, R.layout.item_list_video, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        arrVideo = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolderItem = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_video, parent, false);
            viewHolderItem = new ViewHolderItem();
            viewHolderItem.imgThumbail = (ImageView) convertView.findViewById(R.id.img_thumbail);
            viewHolderItem.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            viewHolderItem.btnPlay = (ImageButton) convertView.findViewById(R.id.btn_play);
            viewHolderItem.btnDelete = (ImageButton) convertView.findViewById(R.id.btn_delete);
            viewHolderItem.btnShare = (ImageButton) convertView.findViewById(R.id.btn_share);
            convertView.setTag(viewHolderItem);
        } else {
            viewHolderItem = (ViewHolderItem) convertView.getTag();
        }

        viewHolderItem.txtName.setText(arrVideo.get(position).getNameVideo());
        viewHolderItem.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commucationAdapter.onClickDeleteBtn(arrVideo.get(position).getFilePath(), position);
            }
        });
        viewHolderItem.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File f = new File(arrVideo.get(position).getFilePath());
                final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("video/mp4");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
                context.startActivity(Intent.createChooser(shareIntent, "Share video using"));
            }
        });
        viewHolderItem.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(arrVideo.get(position).getFilePath());
                Intent movieIntent = new Intent();
                movieIntent.setAction(android.content.Intent.ACTION_VIEW);
                movieIntent.setDataAndType(Uri.fromFile(file), "video/*");
                context.startActivity(movieIntent);
            }
        });

        Glide.with(context)
                .load(arrVideo.get(position).getFilePath())
                .asBitmap()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .videoDecoder(getThumbnailVideo(context))
                .into(viewHolderItem.imgThumbail);

        return convertView;

    }

    public static FileDescriptorBitmapDecoder getThumbnailVideo(Context context){
        BitmapPool bitmapPool = Glide.get(context).getBitmapPool();
        int microSecond = 100000; //seconds number 1 in video
        VideoBitmapDecoder videoBitmapDecoder = new VideoBitmapDecoder(microSecond);
        return new FileDescriptorBitmapDecoder(videoBitmapDecoder, bitmapPool, DecodeFormat.PREFER_ARGB_8888);
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    class ViewHolderItem {
        TextView txtName;
        ImageView imgThumbail;
        ImageButton btnPlay;
        ImageButton btnDelete;
        ImageButton btnShare;
    }

    public void listenerAdapter(CommucationAdapter commucationAdapter) {
        this.commucationAdapter = commucationAdapter;
    }

    public interface CommucationAdapter {
        void onClickDeleteBtn(String pathFile, int position);
    }
}
