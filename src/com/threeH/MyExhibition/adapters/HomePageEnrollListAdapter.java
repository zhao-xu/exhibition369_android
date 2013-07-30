package com.threeH.MyExhibition.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.cache.XmlDB;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.entities.EnrollExhibition;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.service.FileService;
import com.threeH.MyExhibition.service.ImageService;
import com.threeH.MyExhibition.tools.ImageURLUtil;
import com.threeH.MyExhibition.tools.MSYH;
import com.threeH.MyExhibition.tools.SharedPreferencesUtil;
import com.threeH.MyExhibition.tools.Tool;
import com.threeH.MyExhibition.ui.HomeActivity;
import com.threeH.MyExhibition.ui.HomeOfTabActivity;
import com.threeH.MyExhibition.ui.SignupActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class HomePageEnrollListAdapter extends BaseAdapter {

    private List<Exhibition> data;
    private LayoutInflater mInflater;
    private Context context;
    private String status;
    private String token;
    private List<HashMap<String,String>> mListMyexhibiton =
                new ArrayList<HashMap<String, String>>();
    Typeface typeface;
    Typeface typeface_bold;

    public HomePageEnrollListAdapter(Context context, List<Exhibition> data,String token) {
        this.data = data;
        mInflater = LayoutInflater.from(context);
        this.context = context;
        typeface = MSYH.getInstance(context.getApplicationContext()).getNormal();
        typeface_bold = MSYH.getInstance(context.getApplicationContext()).getBold();
        this.token = token;
        initMyexhibitonList();
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
        if(!isMyExhibiton(data.get(position).getExKey())){
            holder.mEnrollAttention.setImageResource(R.drawable.attention);
            holder.mEnrollAttention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferencesUtil.saveObject(data.get(i),context, StringPools.SCAN_EXHIBITION_DATA);
                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
        int count = Integer.valueOf(data.get(position).getCount());
        if(count > 0){
           holder.mEnrollMessage.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    /**
     * 获取本地存储展会数据，总共两部分
     * 1.关注的展会  2.已报名的展会
     */
    private void  initMyexhibitonList(){
        String jsonData = XmlDB.getInstance(context).
                          getKeyStringValue(StringPools.SIGNUP_EXHIBITION_DATA, "");
        EnrollExhibition.EnrollStatus[] myExhibitons =
                new Gson().fromJson(jsonData, EnrollExhibition.EnrollStatus[].class);
        List<Object> list  =
                SharedPreferencesUtil.getObject(context, StringPools.SCAN_EXHIBITION_DATA);
        if(list != null){
            for(Object object : list){
                addToList(((Exhibition)object).getExKey());
            }
        }
        for(EnrollExhibition.EnrollStatus mEnrollStatus : myExhibitons){
            addToList(mEnrollStatus.getExKey());
        }
    }

    /**
     * 添加到我的展会列表
     * @param exKey
     */
    private void addToList(String exKey){
        HashMap<String,String> map =new HashMap<String,String>();
        map.put("exhibitionExkey",exKey);
        mListMyexhibiton.add(map);
    }

    /**
     * 判断该展会是否已经存在在我的展会列表中
     * @param exkey 展会标识
     * @return 存在返回true 不存在返回false
     */
    private boolean isMyExhibiton(String exkey){
        for (HashMap<String, String> hashMap : mListMyexhibiton) {
            if (hashMap.get("exhibitionExkey").contains(exkey)) {
                return true;
            }
        }
        return false;
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
