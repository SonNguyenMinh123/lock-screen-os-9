package com.abc.xyz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abc.xyz.R;
import com.abc.xyz.entities.MessageEntity;

import java.util.Calendar;
import java.util.List;

public class MessagerAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<MessageEntity> mListMessage;

    public MessagerAdapter(Context context, List<MessageEntity> listMessage) {
        mContext = context;
        mListMessage = listMessage;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mListMessage.size();
    }

    @Override
    public MessageEntity getItem(int position) {
        return mListMessage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.item_slv_message, parent, false);
            vh = ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        MessageEntity item = getItem(position);
        vh.txvItemSlvMessageTitle.setText(item.getTitle());
        Calendar calendar = Calendar.getInstance();
        long milliseconds = calendar.getTimeInMillis() - item.getTime();
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        String myTime = "";
        if (hours > 0) {
            myTime += hours + "h:";
        }
        if (minutes > 0) {
            myTime += minutes + "m :";
        }
        if (seconds > 0) {
            myTime += seconds + "s ago";
        }
        vh.txvItemSlvMessageTime.setText(myTime);
        vh.txvItemSlvMessageContent.setText(item.getContent());
        vh.imageView.setImageResource(item.getIcon());
        // TODOBind your data to the views here

        return vh.rootView;
    }

    private static class ViewHolder {
        public final LinearLayout rootView;
        public final ImageButton imageView;
        public final TextView txvItemSlvMessageTitle;
        public final TextView txvItemSlvMessageTime;
        public final TextView txvItemSlvMessageContent;

        private ViewHolder(LinearLayout rootView, ImageButton imageView, TextView txvItemSlvMessageTitle, TextView txvItemSlvMessageTime, TextView txvItemSlvMessageContent) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.txvItemSlvMessageTitle = txvItemSlvMessageTitle;
            this.txvItemSlvMessageTime = txvItemSlvMessageTime;
            this.txvItemSlvMessageContent = txvItemSlvMessageContent;
        }

        public static ViewHolder create(LinearLayout rootView) {
            ImageButton imageView = (ImageButton) rootView.findViewById(R.id.imageView);
            TextView txvItemSlvMessageTitle = (TextView) rootView.findViewById(R.id.txv_item_slv_message_title);
            TextView txvItemSlvMessageTime = (TextView) rootView.findViewById(R.id.txv_item_slv_message_time);
            TextView txvItemSlvMessageContent = (TextView) rootView.findViewById(R.id.txv_item_slv_message_content);
            return new ViewHolder(rootView, imageView, txvItemSlvMessageTitle, txvItemSlvMessageTime, txvItemSlvMessageContent);
        }
    }
}
