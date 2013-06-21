package com.threeH.MyExhibition.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.ui.SignupActivity;
import com.threeH.MyExhibition.ui.SignupExhiListActivity;

import java.util.HashMap;
import java.util.List;


public class HomePageEnrollListAdapter extends BaseAdapter {

    private List<HashMap<String, String>> data;
    private LayoutInflater mInflater;
    private Context context;
    public HomePageEnrollListAdapter(Context context, List<HashMap<String, String>> data) {
        this.data = data;
        mInflater = LayoutInflater.from(context);
        context = this.context;
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
            holder.mEnrollSignup = (ImageView) convertView.findViewById(R.id.button_signup);
            holder.mExhibitionName = (TextView) convertView.findViewById(R.id.exhibition_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mExhibitionName.setText( data.get(position).get("exhibitionName"));
        /*holder.mEnrollSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SignupActivity.class);
            }
        });*/
        return convertView;
    }

    public class ViewHolder {
        TextView mExhibitionName;
        //ImageButton mEnrollSignup;
        ImageView mEnrollSignup;
    }
}
