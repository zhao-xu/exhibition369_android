package com.threeH.MyExhibition.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.cache.XmlDB;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.entities.UnEnrollExhibition;
import com.threeH.MyExhibition.tools.ImageURLUtil;
import com.threeH.MyExhibition.tools.MSYH;
import com.threeH.MyExhibition.tools.Tool;
import java.util.HashMap;
import java.util.List;


public class SignExhiListAdapter extends BaseAdapter {
    private List<HashMap<String, String>> data;
    private LayoutInflater mInflater;
    private Context context;
    Typeface typeface;
    Typeface typeface_bold;
    public SignExhiListAdapter(Context context, List<HashMap<String, String>> data) {
        this.data = data;
        mInflater = LayoutInflater.from(context);
        this.context = context;
        typeface = MSYH.getInstance(context.getApplicationContext()).getNormal();
        typeface_bold = MSYH.getInstance(context.getApplicationContext()).getBold();
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
            holder.mEnrollMessage = (ImageView) convertView.findViewById(R.id.message_icon_signup);

            holder.mExhibitionTheme.setTypeface(typeface_bold);
            holder.mExhibitionDate.setTypeface(typeface);
            holder.mExhibitionAddress.setTypeface(typeface);
            holder.mExhibitionSponsor.setTypeface(typeface);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.mSignupStatus.setImageBitmap(null);
            holder.mEnrollMessage.setVisibility(View.GONE);
            holder.mSignupStatus.setPadding(0,0,0,0);
            holder.mExhibitionIcon.setImageBitmap(null);
        }
        char showStatus = ' ';
        if(null != data.get(position).get("status")){
            showStatus = data.get(position).get("status").charAt(0);
            switch (showStatus){
                case 'P':
                    holder.mSignupStatus.setImageResource(R.drawable.examine);
                    break;
                case 'A':
                    holder.mSignupStatus.setImageResource(R.drawable.pass);
                    holder.mSignupStatus.setPadding(0, 5, 0, 0);
                    break;
                case 'D':
                    holder.mSignupStatus.setImageResource(R.drawable.no_pass);
                    holder.mSignupStatus.setPadding(0, 60, 0, 0);
                    break;
            }
        }
        holder.mExhibitionTheme.setText(data.get(position).get("name"));
        holder.mExhibitionSponsor.setText(data.get(position).get("organizer"));
        holder.mExhibitionAddress.setText(data.get(position).get("address"));
        holder.mExhibitionDate.setText(data.get(position).get("date"));

        ImageURLUtil.loadImage(Tool.makeExhibitionIconURL(exKey),
                holder.mExhibitionIcon);
        int count = Integer.valueOf(data.get(position).get("count"));
        if(count > 0){
            holder.mEnrollMessage.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private Exhibition getExhibitionData(String exKey) {
        String strExhibitionData = XmlDB.getInstance(context).getKeyStringValue(StringPools.ALL_EXHIBITION_DATA, "");
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
        ImageView mEnrollMessage;
    }
}
