package com.threeH.MyExhibition.ui;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.*;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.entities.UnEnrollExhibition;
import com.threeH.MyExhibition.listener.AttentionClickListener;
import com.threeH.MyExhibition.listener.SignupClickListener;
import com.threeH.MyExhibition.listener.TelephoneClickListener;
import com.threeH.MyExhibition.tools.MSYH;
import com.threeH.MyExhibition.tools.MyExhibitionListUtil;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-6-20
 * Time: 上午10:40
 * To change this template use File | Settings | File Templates.
 */
public class ExhibitionBriefActivity extends BaseActivity implements ActivityInterface  {
    private WebView mWebVi;
    private String mUrl;
    private ImageView mImgviewTelephone, mImgviewSignup;
    private TextView mTxtTitle,mTxtTheme;
    private String mStrTitle,mStrTheme;
    private char mChrStatus;
    private String mStrExKey;
    private Button mBtnSignup;
    Typeface typeface;
    FrameLayout webContainer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.exhibition_brief);

        webContainer = (FrameLayout) findViewById(R.id.exhibition_brief_webContainer);
        mWebVi = new WebView(getApplicationContext());
        webContainer.addView(mWebVi);

        initdata();
        findView();
        addAction();
    }

    @Override
    protected void onDestroy() {
        webContainer.removeAllViews();
        mWebVi.destroy();
        super.onDestroy();
    }

    @Override
    public void findView() {
        mImgviewTelephone = (ImageView) this.findViewById(R.id.exhibition_titlebar_btn_telephone);
        mTxtTitle = (TextView) this.findViewById(R.id.exhibition_titlebar_txt_title);
        mImgviewSignup = (ImageView) this.findViewById(R.id.exhibition_titlebar_signup);
        mTxtTheme = (TextView) this.findViewById(R.id.exhibition_brief_txt_theme);
        mBtnSignup = (Button) this.findViewById(R.id.exhibition_brief_btn_signup);
    }

    @Override
    public void initdata() {
        mUrl = getIntent().getStringExtra("url");
        mStrTitle = getIntent().getStringExtra("title");
        mChrStatus = getIntent().getCharExtra("singupStatus", ' ');
        mStrExKey = getIntent().getStringExtra("exKey");
        mStrTheme = getIntent().getStringExtra("theme");
        typeface = MSYH.getInstance(context.getApplicationContext()).getNormal();
        MyExhibitionListUtil.getInstance(context).initMyExhiibitonList();
    }

    @Override
    public void addAction() {
        mImgviewTelephone.setOnClickListener(new TelephoneClickListener(this, tel_nummber));
        mTxtTitle.setTypeface(typeface);
        mTxtTitle.setText(mStrTitle);
        mImgviewSignup.setVisibility(View.GONE);
        if(MyExhibitionListUtil.getInstance(context).isMyExhibiton(mStrExKey)){
            switch (mChrStatus){
                case ' ':
                case 'D':
                case 'N':
                    mBtnSignup.setOnClickListener(new SignupClickListener(this, mStrExKey));
                    break;
                case 'P':
                case 'A':
                    mBtnSignup.setVisibility(View.GONE);
                    break;
            }
        }else{
            mBtnSignup.setBackgroundResource(R.drawable.attention_font_btn);
            String str = null;
            try {
                str = mController.getService().UnErollExListByExKey(token, 1, -1, mStrExKey);
                UnEnrollExhibition exhibitions = new Gson().fromJson(str,UnEnrollExhibition.class);
                if(exhibitions.getList() != null){
                    mBtnSignup.setOnClickListener(
                            new AttentionClickListener(context,exhibitions.getList().get(0)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mTxtTheme.setText(mStrTheme);
        LoadHtmlTask loadHtmlTask = new LoadHtmlTask();
        loadHtmlTask.execute();
    }
    class LoadHtmlTask extends AsyncTask<Void,Integer,Integer>{
        @Override
        protected Integer doInBackground(Void... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mWebVi.loadUrl(mUrl);
                }
            }).start();
            return null;
        }
    }

}
