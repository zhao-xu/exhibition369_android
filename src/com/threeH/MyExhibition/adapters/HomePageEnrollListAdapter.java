package com.threeH.MyExhibition.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.tools.ImageURLUtil;
import com.threeH.MyExhibition.tools.MSYH;
import com.threeH.MyExhibition.tools.Tool;
import com.threeH.MyExhibition.ui.SignupActivity;

import java.util.HashMap;
import java.util.List;


public class HomePageEnrollListAdapter extends BaseAdapter {

    private List<HashMap<String, String>> data;
    private LayoutInflater mInflater;
    private Context context;
    private String status;
    Typeface typeface;
    Typeface typeface_bold;

    public HomePageEnrollListAdapter(Context context, List<HashMap<String, String>> data) {
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.mInflater.inflate(R.layout.head_page_enroll_item, null);
            holder.mEnrollSignup = (ImageView) convertView.findViewById(R.id.imageview_signup);
            holder.mExhibitionTheme = (TextView) convertView.findViewById(R.id.exhibition_theme);
            holder.mExhibitionDate = (TextView) convertView.findViewById(R.id.exhibition_date);
            holder.mExhibitionAddress = (TextView) convertView.findViewById(R.id.exhibition_address);
            holder.mExhibitionSponser = (TextView) convertView.findViewById(R.id.exhibition_sponsor);
            holder.mExhibitionIcon = (ImageView) convertView.findViewById(R.id.imageview_icon);

            holder.mExhibitionTheme.setTypeface(typeface_bold);
            holder.mExhibitionDate.setTypeface(typeface);
            holder.mExhibitionAddress.setTypeface(typeface);
            holder.mExhibitionSponser.setTypeface(typeface);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.mEnrollSignup.setImageBitmap(null);
            holder.mExhibitionIcon.setImageBitmap(null);
            holder.mEnrollSignup.setPadding(0,0,0,0);
        }
        holder.mExhibitionTheme.setText(data.get(position).get("exhibitionName"));
        holder.mExhibitionDate.setText(data.get(position).get("exhibitionDate"));
        holder.mExhibitionAddress.setText(data.get(position).get("exhibitionAddress"));
        holder.mExhibitionSponser.setText(data.get(position).get("exhibitionSponser"));
        ImageURLUtil.loadImage(Tool.makeExhibitionIconURL(data.get(position).get("exhibitionExkey")),
                               holder.mExhibitionIcon);
        status = data.get(position).get("exhibitionApplied");
        if(null != status && "N".equals(status)){
             holder.mEnrollSignup.setImageResource(R.drawable.signup);
        }
        char  showStatus = ' ';
        if(null != data.get(position).get("status") && !"".equals(data.get(position).get("status"))){
            showStatus = data.get(position).get("status").charAt(0);
            switch (showStatus){
                case 'P':
                    holder.mEnrollSignup.setImageResource(R.drawable.examine);
                    holder.mEnrollSignup.setPadding(0, 60, 0, 0);
                    break;
                case 'A':
                    holder.mEnrollSignup.setImageResource(R.drawable.pass);
                    break;
                case 'D':
                    holder.mEnrollSignup.setImageResource(R.drawable.no_pass);
                    holder.mEnrollSignup.setPadding(0, 60, 0, 0);
                    break;
            }

        }

        final int i = position;
        holder.mEnrollSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SignupActivity.class);
                intent.putExtra("exKey",data.get(i).get("exhibitionExkey"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        ImageView mExhibitionIcon;
        TextView mExhibitionTheme;
        TextView mExhibitionDate;
        TextView mExhibitionAddress;
        TextView mExhibitionSponser;
        ImageView mEnrollSignup;
    }
}
