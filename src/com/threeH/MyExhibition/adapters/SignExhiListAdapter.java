package com.threeH.MyExhibition.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.listener.SignupClickListener;
import com.threeH.MyExhibition.service.FileService;
import com.threeH.MyExhibition.service.ImageService;
import com.threeH.MyExhibition.tools.ImageURLUtil;
import com.threeH.MyExhibition.tools.MSYH;
import com.threeH.MyExhibition.tools.SharedPreferencesUtil;
import com.threeH.MyExhibition.tools.Tool;

import java.util.List;


public class SignExhiListAdapter extends BaseAdapter {
    private List<Exhibition> data;
    private LayoutInflater mInflater;
    private Context context;
    private String mStrToken;
    Typeface typeface;
    Typeface typeface_bold;
    public SignExhiListAdapter(Context context, List<Exhibition> data) {
        this.data = data;
        mInflater = LayoutInflater.from(context);
        this.context = context;
        typeface = MSYH.getInstance(context.getApplicationContext()).getNormal();
        typeface_bold = MSYH.getInstance(context.getApplicationContext()).getBold();
    }

    public SignExhiListAdapter(Context context, List<Exhibition> data,String token) {
        this(context,data);
        this.mStrToken = token;
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
        String exKey = data.get(position).getExKey();
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
            holder.mSignupStatus.setPadding(0, 0, 0, 0);
            holder.mSignupStatus.setOnClickListener(null);
            holder.mExhibitionIcon.setImageBitmap(null);
        }
        char showStatus = ' ';
        if(null != data.get(position).getStatus()){
            showStatus = (data.get(position).getStatus() + " ").charAt(0);
        }
        showStatusIcon(showStatus,holder.mSignupStatus,exKey,position);
        holder.mExhibitionTheme.setText(data.get(position).getName());
        holder.mExhibitionSponsor.setText(data.get(position).getOrganizer());
        holder.mExhibitionAddress.setText(data.get(position).getAddress());
        holder.mExhibitionDate.setText(data.get(position).getDate());
        ImageURLUtil.loadImage(Tool.makeExhibitionIconURL(exKey),
                holder.mExhibitionIcon);
        int count = Integer.valueOf(data.get(position).getCount());
        if(count > 0){
            holder.mEnrollMessage.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
    /**
     * 根据审核的状态显示各个展会的状态图片
     * 状态为 P: 表示正在审核中
     * 状态为 A: 表示正在审核通过
     * 状态为 D: 表示正在审核未通过
     * 状态为 ' ': 表示正在未报名该展会
     * @param showStatus 审核状态
     * @param imageView 显示状态图片的控件
     * @param exKey     展会的key
     * @param position  该展会在数据列表中的索引位置
     */
    private void showStatusIcon(char showStatus,ImageView imageView,
                                final String exKey,final int position){
        switch (showStatus){
            case 'P':
                imageView.setImageResource(R.drawable.examine);
                break;
            case 'A':
                imageView.setImageResource(R.drawable.pass);
                SaveQrcodeTask saveQrcodeTask = new SaveQrcodeTask(exKey);
                saveQrcodeTask.execute();
                break;
            case 'D':
                imageView.setImageResource(R.drawable.no_pass);
                break;
            default:
                imageView.setImageResource(R.drawable.signup_font_btn);
                imageView.setOnClickListener(new SignupClickListener(context,exKey));
                /*imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(context)
                                .setTitle("注意")
                                .setMessage("您确认要删除该展会吗？")
                                .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPreferencesUtil.removeObject(exKey,context,StringPools.SCAN_EXHIBITION_DATA);
                                        data.remove(position);
                                        notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("取消",null)
                                .show();
                    }
                });*/
                break;
        }
    }

    public class ViewHolder {
        TextView mExhibitionTheme, mExhibitionDate, mExhibitionAddress, mExhibitionSponsor;
        ImageView mExhibitionIcon,mSignupStatus;
        ImageView mEnrollMessage;
    }

    class SaveQrcodeTask extends AsyncTask<Void,Integer,Integer> {
        private String exKey;

        SaveQrcodeTask(String exKey) {
            this.exKey = exKey;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String path = Tool.makeQrcodeURL(exKey,mStrToken);
                    try {
                        byte[] data = ImageService.getImage(path);
                        FileService service = new FileService(context);
                        String filename = exKey + "qrcode.png";
                        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                            service.saveToSDCard(filename, data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            return null;
        }
    }
}