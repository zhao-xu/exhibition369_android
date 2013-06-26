package com.threeH.MyExhibition.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.threeH.MyExhibition.R;

import java.util.HashMap;
import java.util.List;


public class MessageListAdapter extends BaseAdapter {

    private List<HashMap<String, String>> data;
    private LayoutInflater mInflater;
    Typeface typeface_bold;

    public MessageListAdapter(Context context, List<HashMap<String, String>> data) {
        this.data = data;
        mInflater = LayoutInflater.from(context);
        typeface_bold = Typeface.createFromAsset(context.getAssets(), "fonts/msyhbd.ttf");
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
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
        holder.mMessageDate.setText(data.get(position).get("date"));
        holder.mMessageContent.setText(data.get(position).get("content"));
        return convertView;
    }

    public class ViewHolder {
        TextView mMessageDate, mMessageContent;
        ImageView mMessageIcon,mItemBtn;
        LinearLayout mLinearLayout;
    }
}
