package com.threeH.MyExhibition.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.entities.UnEnrollExhibition;
import com.threeH.MyExhibition.tools.ImageURLUtil;
import com.threeH.MyExhibition.tools.Tool;
import java.util.HashMap;
import java.util.List;


public class SignExhiListAdapter extends BaseAdapter {
    private List<HashMap<String, String>> data;
    private LayoutInflater mInflater;
    private Context context;
    public SignExhiListAdapter(Context context, List<HashMap<String, String>> data) {
        this.data = data;
        mInflater = LayoutInflater.from(context);
        this.context = context;
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
        String exKey = data.get(position).get("exhibitionExkey");
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.mInflater.inflate(R.layout.signup_exhi_list_item, null);
            holder.mExhibitionIcon = (ImageView) convertView.findViewById(R.id.signup_list_imageview_icon);
            holder.mExhibitionTheme = (TextView) convertView.findViewById(R.id.signup_list_exhibition_theme);
            holder.mExhibitionDate = (TextView) convertView.findViewById(R.id.signup_list_exhibition_date);
            holder.mExhibitionAddress = (TextView) convertView.findViewById(R.id.signup_list_exhibition_address);
            holder.mExhibitionSponsor = (TextView) convertView.findViewById(R.id.signup_list_exhibition_sponsor);
            holder.mSignupStatus = (ImageView) convertView.findViewById(R.id.signup_list_imageview_signup);
            //holder.mLinearLayout = (LinearLayout) convertView.findViewById(R.id.status_titlebar);
            //holder.mStatusBarText = (TextView)convertView.findViewById(R.id.status_bar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.mSignupStatus.setImageBitmap(null);
        }
        Exhibition exhibition = getExhibitionData(exKey);
        char showStatus = ' ';
        String hideStatus = "";
        //P 审核中(Processing)，A 审核通过(Approved)，D 审核未通过(Denied)
        /*if(null != data.get(position).get("status")){
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
        }*/
        if(null != data.get(position).get("status")){
            showStatus = data.get(position).get("status").charAt(0);
            switch (showStatus){
                case 'P':
                    holder.mSignupStatus.setBackgroundResource(R.drawable.examine);
                    break;
                case 'A':
                    holder.mSignupStatus.setBackgroundResource(R.drawable.pass);
                    break;
                case 'D':
                    holder.mSignupStatus.setBackgroundResource(R.drawable.no_pass);
                    break;
            }
        }
        holder.mExhibitionTheme.setText(data.get(position).get("name"));
        holder.mExhibitionSponsor.setText(exhibition.getOrganizer());
        holder.mExhibitionAddress.setText(exhibition.getAddress());
        holder.mExhibitionDate.setText(exhibition.getDate());

        ImageURLUtil.loadImage(Tool.makeExhibitionIconURL(exKey),
                holder.mExhibitionIcon);
        return convertView;
    }

    private Exhibition getExhibitionData(String exKey) {
        SharedPreferences sharedPreferences =   context.getSharedPreferences("allExhibitionData", Activity.MODE_PRIVATE);
        String strExhibitionData = sharedPreferences.getString("exhibitionData",null);
        UnEnrollExhibition allExhibitionData = new Gson().fromJson(strExhibitionData,UnEnrollExhibition.class);
        for(Exhibition exhibition : allExhibitionData.getList()){
            if(null != exKey && exhibition.getExKey().equals(exKey)){
                return exhibition;
            }
        }
        return null;
    }

    public class ViewHolder {
        TextView mExhibitionTheme, mExhibitionDate, mExhibitionAddress, mExhibitionSponsor;
        ImageView mExhibitionIcon,mSignupStatus;
        //LinearLayout mLinearLayout;
        //TextView mStatusBarText;
    }
}
