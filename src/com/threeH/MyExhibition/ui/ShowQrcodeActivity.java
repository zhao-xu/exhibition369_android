package com.threeH.MyExhibition.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.service.FileService;
import com.threeH.MyExhibition.tools.ImageURLUtil;
import com.threeH.MyExhibition.tools.NetworkHelper;
import com.threeH.MyExhibition.tools.Tool;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-8-13
 * Time: 下午7:53
 * To change this template use File | Settings | File Templates.
 */
public class ShowQrcodeActivity extends BaseActivity {
    private ImageView mImgviewQrcode,mImgviewBack;
    private String mStrExkey;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.popwindow);
        mStrExkey = getIntent().getStringExtra("exKey");
        mImgviewQrcode = (ImageView) this.findViewById(R.id.popwindow_imgview_qrcode);
        mImgviewBack = (ImageView) this.findViewById(R.id.popwindow_imgview_finish);
        mImgviewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loadQrcode();
    }

    private void loadQrcode() {
        if(NetworkHelper.getInstance(context).isConnected()){
            ImageURLUtil.loadImage(Tool.makeQrcodeURL(mStrExkey,token), mImgviewQrcode);
        }else{
            FileService service = new FileService(context);
            byte[] data;
            try {
                String filename = Environment.getExternalStorageDirectory() +
                        "/" + mStrExkey + "qrcode.png";
                data = service.read(filename);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                mImgviewQrcode.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
