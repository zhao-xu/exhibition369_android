package com.threeH.MyExhibition.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.threeH.MyExhibition.R;

import java.util.HashMap;
import java.util.List;


public class HomePageSearchListAdapter extends BaseAdapter {

    private List<HashMap<String, String>> data;
    private LayoutInflater mInflater;

    public HomePageSearchListAdapter(Context context, List<HashMap<String, String>> data) {
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
            convertView = this.mInflater.inflate(R.layout.head_page_search_item, null);
            holder.mExhibitionIcon = (ImageView) convertView.findViewById(R.id.exhibition_icon);
            holder.mExhibitionTheme = (TextView) convertView.findViewById(R.id.exhibition_theme);
            holder.mExhibitionDate = (TextView) convertView.findViewById(R.id.exhibition_date);
            holder.mExhibitionAddress = (TextView) convertView.findViewById(R.id.exhibition_address);
            holder.mExhibitionSponsor = (TextView) convertView.findViewById(R.id.exhibition_sponsor);
            holder.mEnrollBtn = (Button) convertView.findViewById(R.id.enroll_btn);

            holder.mExhibitionTheme.setText(data.get(position).get("name"));
            holder.mExhibitionSponsor.setText(data.get(position).get("the_me"));
            holder.mExhibitionAddress.setText(data.get(position).get("address"));
            holder.mExhibitionDate.setText(data.get(position).get("date"));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    public class ViewHolder {
        TextView mExhibitionTheme, mExhibitionDate, mExhibitionAddress, mExhibitionSponsor;
        ImageView mExhibitionIcon;
        Button mEnrollBtn;
    }
}
