package com.threeH.MyExhibition.adapters;

import android.content.Context;
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

    public MessageListAdapter(Context context, List<HashMap<String, String>> data) {
        this.data = data;
        mInflater = LayoutInflater.from(context);
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
            convertView = this.mInflater.inflate(R.layout.message_list_item, null);
            holder.mLinearLayout = (LinearLayout) convertView.findViewById(R.id.message_title);
            holder.mMessageDate = (TextView)convertView.findViewById(R.id.message_date);
            holder.mMessageContent = (TextView)convertView.findViewById(R.id.message_content);
            holder.mMessageIcon =(ImageView)convertView.findViewById(R.id.message_icon);
            holder.mItemBtn = (ImageView)convertView.findViewById(R.id.item_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String showDate = "";
        String hideDate = "";
        //P 审核中(Processing)，A 审核通过(Approved)，D 审核未通过(Denied)
        if(null != data.get(position).get("date")){
            showDate = data.get(position).get("date");
        }
        if(position==0){
            holder.mLinearLayout.setVisibility(View.VISIBLE);
            holder.mLinearLayout.setBackgroundResource(R.drawable.tag_pass);
            holder.mMessageDate.setText(showDate);

        }else {
            if (null != data.get(position).get("date")) {
                 hideDate = data.get(position - 1).get("date");
            }
            if (showDate.equals(hideDate)) {
                holder.mLinearLayout.setVisibility(View.GONE);
            } else {
                holder.mLinearLayout.setVisibility(View.VISIBLE);
                holder.mLinearLayout.setBackgroundResource(R.drawable.tag_pass);
                holder.mMessageDate.setText(showDate);
            }
        }
        holder.mMessageContent.setText(data.get(position).get("content"));
        return convertView;
    }

    public class ViewHolder {
        TextView mMessageDate, mMessageContent;
        ImageView mMessageIcon,mItemBtn;
        LinearLayout mLinearLayout;
    }
}
