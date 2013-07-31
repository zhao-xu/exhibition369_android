package com.threeH.MyExhibition.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.listener.AttentionClickListener;
import com.threeH.MyExhibition.tools.*;

import java.util.List;


public class ExhibitionListAdapter extends BaseAdapter {

    private List<Exhibition> data;
    private LayoutInflater mInflater;
    private Context context;
    private String status;
    private String token;
    Typeface typeface;
    Typeface typeface_bold;

    public ExhibitionListAdapter(Context context, List<Exhibition> data, String token) {
        this.data = data;
        mInflater = LayoutInflater.from(context);
        this.context = context;
        typeface = MSYH.getInstance(context.getApplicationContext()).getNormal();
        typeface_bold = MSYH.getInstance(context.getApplicationContext()).getBold();
        this.token = token;
        MyExhibitionListUtil.getInstance(context).initMyExhiibitonList();
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
            holder.mEnrollAttention = (ImageView) convertView.findViewById(R.id.imageview_attention);
            holder.mExhibitionTheme = (TextView) convertView.findViewById(R.id.exhibition_theme);
            holder.mExhibitionDate = (TextView) convertView.findViewById(R.id.exhibition_date);
            holder.mExhibitionAddress = (TextView) convertView.findViewById(R.id.exhibition_address);
            holder.mExhibitionSponser = (TextView) convertView.findViewById(R.id.exhibition_sponsor);
            holder.mExhibitionIcon = (ImageView) convertView.findViewById(R.id.imageview_icon);
            holder.mEnrollMessage = (ImageView) convertView.findViewById(R.id.message_icon);

            holder.mExhibitionTheme.setTypeface(typeface_bold);
            holder.mExhibitionDate.setTypeface(typeface);
            holder.mExhibitionAddress.setTypeface(typeface);
            holder.mExhibitionSponser.setTypeface(typeface);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.mEnrollAttention.setImageBitmap(null);
            holder.mExhibitionIcon.setImageBitmap(null);
            holder.mEnrollAttention.setPadding(0, 0, 0, 0);
            holder.mEnrollMessage.setVisibility(View.GONE);
            holder.mEnrollAttention.setOnClickListener(null);
        }
        String exKey = data.get(position).getExKey();
        holder.mExhibitionTheme.setText(data.get(position).getName());
        holder.mExhibitionDate.setText(data.get(position).getDate());
        holder.mExhibitionAddress.setText(data.get(position).getAddress());
        holder.mExhibitionSponser.setText(data.get(position).getOrganizer());
        ImageURLUtil.loadImage(Tool.makeExhibitionIconURL(exKey),
                               holder.mExhibitionIcon);
        status = data.get(position).getApplied();

        final int i = position;
        if(!MyExhibitionListUtil.getInstance(context).isMyExhibiton(data.get(position).getExKey())){
            holder.mEnrollAttention.setImageResource(R.drawable.attention);
            holder.mEnrollAttention.setOnClickListener(
                    new AttentionClickListener(context,data.get(i)));
        }
        int count = Integer.valueOf(data.get(position).getCount());
        if(count > 0){
           holder.mEnrollMessage.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public class ViewHolder {
        ImageView mExhibitionIcon;
        TextView mExhibitionTheme;
        TextView mExhibitionDate;
        TextView mExhibitionAddress;
        TextView mExhibitionSponser;
        ImageView mEnrollAttention;
        ImageView mEnrollMessage;
    }
}
