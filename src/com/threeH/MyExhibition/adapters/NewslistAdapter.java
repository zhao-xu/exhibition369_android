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
import com.threeH.MyExhibition.tools.ImageURLUtil;
import com.threeH.MyExhibition.tools.MSYH;
import com.threeH.MyExhibition.tools.Tool;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-6-24
 * Time: 上午9:13
 * To change this template use File | Settings | File Templates.
 */
public class NewslistAdapter extends BaseAdapter {
    private List<HashMap<String, String>> data;
    private LayoutInflater mInflater;
    private Context context;
    Typeface typeface;
    Typeface typeface_bold;

    public NewslistAdapter(List<HashMap<String, String>> data, Context context) {
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
        if(null == convertView){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.newslist_item,null);
            holder.mNewsIcon = (ImageView) convertView.findViewById(R.id.newslist_item_imageview);
            holder.mNewsTitle = (TextView) convertView.findViewById(R.id.newslist_item_textview_newstitle);

            holder.mNewsTitle.setTypeface(typeface_bold);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
            //holder.mNewsIcon.setImageBitmap(null);
        }
        String exKey = data.get(position).get("exKey");
        String newsKey = data.get(position).get("newsKey");

        ImageURLUtil.loadImage(Tool.makeNewsIconURL(exKey,newsKey),holder.mNewsIcon);
        if(null == holder.mNewsIcon.getDrawable()){
            ImageURLUtil.loadImage(Tool.makeExhibitionIconURL(exKey),holder.mNewsIcon);
        }
        holder.mNewsTitle.setText(data.get(position).get("newsTitle"));
        return convertView;
    }

    public class ViewHolder {
        ImageView mNewsIcon;
        TextView mNewsTitle;
    }
}
