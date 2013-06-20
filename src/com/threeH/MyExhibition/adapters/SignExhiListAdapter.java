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


public class SignExhiListAdapter extends BaseAdapter {

    private List<HashMap<String, String>> data;
    private LayoutInflater mInflater;

    public SignExhiListAdapter(Context context, List<HashMap<String, String>> data) {
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
            convertView = this.mInflater.inflate(R.layout.signup_exhi_list_item, null);
            holder.mExhibitionIcon = (ImageView) convertView.findViewById(R.id.exhibition_icon);
            holder.mExhibitionTheme = (TextView) convertView.findViewById(R.id.exhibition_theme);
            holder.mExhibitionDate = (TextView) convertView.findViewById(R.id.exhibition_date);
            holder.mExhibitionAddress = (TextView) convertView.findViewById(R.id.exhibition_address);
            holder.mExhibitionSponsor = (TextView) convertView.findViewById(R.id.exhibition_sponsor);
            holder.mLinearLayout = (LinearLayout) convertView.findViewById(R.id.status_titlebar);
            holder.mStatusBarText = (TextView)convertView.findViewById(R.id.status_bar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String showStatus = "";
        String hideStatus = "";
        //P 审核中(Processing)，A 审核通过(Approved)，D 审核未通过(Denied)
        if(null != data.get(position).get("status")){
            showStatus = data.get(position).get("status");
        }
        if(position==0){
            holder.mLinearLayout.setVisibility(View.VISIBLE);
            holder.mLinearLayout.setBackgroundResource(R.drawable.tag_pass);
            holder.mStatusBarText.setText("通过");

        }else {
            if (null != data.get(position).get("status")) {
                 hideStatus = data.get(position - 1).get("status");
            }
            if (showStatus.equals(hideStatus)) {
                holder.mLinearLayout.setVisibility(View.GONE);
            } else {
                holder.mLinearLayout.setVisibility(View.VISIBLE);
                if("P".equals(data.get(position).get("status"))){
                    holder.mLinearLayout.setBackgroundResource(R.drawable.tag_check);
                    holder.mStatusBarText.setText("审核中");
                }else if("A".equals(data.get(position).get("status"))){
                    holder.mLinearLayout.setBackgroundResource(R.drawable.tag_pass);
                    holder.mStatusBarText.setText("通过");
                }else {
                    holder.mLinearLayout.setBackgroundResource(R.drawable.tag_signed);
                    holder.mStatusBarText.setText("未通过");
                }
            }
        }

        holder.mExhibitionTheme.setText(data.get(position).get("name"));
        holder.mExhibitionSponsor.setText(data.get(position).get("the_me"));
        holder.mExhibitionAddress.setText(data.get(position).get("address"));
        holder.mExhibitionDate.setText(data.get(position).get("date"));
        return convertView;
    }

    public class ViewHolder {
        TextView mExhibitionTheme, mExhibitionDate, mExhibitionAddress, mExhibitionSponsor;
        ImageView mExhibitionIcon;
        LinearLayout mLinearLayout;
        TextView mStatusBarText;
    }
}
