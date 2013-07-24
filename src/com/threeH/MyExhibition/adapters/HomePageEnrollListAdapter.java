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
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.service.FileService;
import com.threeH.MyExhibition.service.ImageService;
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
    private String token;
    Typeface typeface;
    Typeface typeface_bold;

    public HomePageEnrollListAdapter(Context context, List<HashMap<String, String>> data,String token) {
        this.data = data;
        mInflater = LayoutInflater.from(context);
        this.context = context;
        typeface = MSYH.getInstance(context.getApplicationContext()).getNormal();
        typeface_bold = MSYH.getInstance(context.getApplicationContext()).getBold();
        this.token = token;
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
        String exKey = data.get(position).get("exhibitionExkey");
        holder.mExhibitionTheme.setText(data.get(position).get("exhibitionName"));
        holder.mExhibitionDate.setText(data.get(position).get("exhibitionDate"));
        holder.mExhibitionAddress.setText(data.get(position).get("exhibitionAddress"));
        holder.mExhibitionSponser.setText(data.get(position).get("exhibitionSponser"));
        ImageURLUtil.loadImage(Tool.makeExhibitionIconURL(exKey),
                               holder.mExhibitionIcon);
        status = data.get(position).get("exhibitionApplied");
        /*final int i = position;
        if(null != status && "N".equals(status)){
            holder.mEnrollAttention.setImageResource(R.drawable.signup);
            holder.mEnrollAttention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SignupActivity.class);
                    intent.putExtra("exKey", data.get(i).get("exhibitionExkey"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }*/
        holder.mEnrollAttention.setImageResource(R.drawable.attention);
        /*char  showStatus = ' ';
        if(null != data.get(position).get("status") && !"".equals(data.get(position).get("status"))){
            showStatus = data.get(position).get("status").charAt(0);
            switch (showStatus){
                case 'P':
                    holder.mEnrollAttention.setImageResource(R.drawable.examine);
                    break;
                case 'A':
                    holder.mEnrollAttention.setImageResource(R.drawable.pass);
                    holder.mEnrollAttention.setPadding(0, 5, 0, 0);
                    SaveQrcodeTask saveQrcodeTask = new SaveQrcodeTask(exKey);
                    saveQrcodeTask.execute();
                    break;
                case 'D':
                    holder.mEnrollAttention.setImageResource(R.drawable.no_pass);
                    holder.mEnrollAttention.setPadding(0, 60, 0, 0);
                    break;
            }

        }*/

        int count = Integer.valueOf(data.get(position).get("count"));
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

    class SaveQrcodeTask extends AsyncTask<Void,Integer,Integer>{
        private String exKey;

        SaveQrcodeTask(String exKey) {
            this.exKey = exKey;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String path = Tool.makeQrcodeURL(exKey,token);
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
