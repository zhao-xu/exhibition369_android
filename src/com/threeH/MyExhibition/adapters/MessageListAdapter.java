package com.threeH.MyExhibition.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.entities.MessageList;
import com.threeH.MyExhibition.tools.MSYH;

import java.util.Date;
import java.util.List;


public class MessageListAdapter extends BaseAdapter {

    private List<MessageList.Message> mData;
    private LayoutInflater mInflater;
    public static int selectedID;
    Typeface typeface_bold;

    public MessageListAdapter(Context context, List<MessageList.Message> data) {
        this.mData = data;
        mInflater = LayoutInflater.from(context);
        typeface_bold = MSYH.getInstance(context.getApplicationContext()).getBold();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.mInflater.inflate(R.layout.messagelist_item, null);
            holder.mMessageDate = (TextView)convertView.findViewById(R.id.message_date);
            holder.mMessageContent = (TextView)convertView.findViewById(R.id.message_content);
            holder.mMessageContent.setTypeface(typeface_bold);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mMessageDate.setText(new Date(mData.get(position).getCreatedAt()).toLocaleString());
        holder.mMessageContent.setText(mData.get(position).getContent());
        if(position == selectedID){
            holder.mMessageContent.setSingleLine(false);
        }else {
            holder.mMessageContent.setSingleLine(true);
        }
        return convertView;
    }

    public class ViewHolder {
        TextView mMessageDate, mMessageContent;
    }
}
