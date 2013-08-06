package com.threeH.MyExhibition.ui;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.*;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.entities.ExhibitionList;
import com.threeH.MyExhibition.listener.AttentionClickListener;
import com.threeH.MyExhibition.listener.SignupClickListener;
import com.threeH.MyExhibition.listener.TelephoneClickListener;
import com.threeH.MyExhibition.tools.MSYH;
import com.threeH.MyExhibition.tools.MyExhibitionListUtil;
import com.threeH.MyExhibition.tools.SharedPreferencesUtil;

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
    private TextView mTxtTheme;
    private Button mBtnSignup;
    private Exhibition mExhibition;
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
        mTxtTheme = (TextView) this.findViewById(R.id.exhibition_brief_txt_theme);
        mBtnSignup = (Button) this.findViewById(R.id.exhibition_brief_btn_signup);
    }

    @Override
    public void initdata() {
        mUrl = getIntent().getStringExtra("url");
        mExhibition = (Exhibition) getIntent().getExtras().get("exhibition");
        MyExhibitionListUtil.getInstance(context).initMyExhiibitonList();
    }

    @Override
    public void addAction() {
        String status = mExhibition.getStatus() + " ";
        char c = status.charAt(0);
        if(MyExhibitionListUtil.getInstance(context).isMyExhibiton(mExhibition.getExKey())){
            if(null != mExhibition.getApplied() && "N".equals(mExhibition.getApplied().trim())){
                mBtnSignup.setOnClickListener(new SignupClickListener(this, mExhibition.getExKey()));
            }else {
                switch (c){
                    case ' ':
                    case 'D':
                    case 'N':
                        mBtnSignup.setOnClickListener(new SignupClickListener(this, mExhibition.getExKey()));
                        break;
                    case 'P':
                    case 'A':
                        mBtnSignup.setVisibility(View.GONE);
                        break;
                }
            }
        }else{
            mBtnSignup.setBackgroundResource(R.drawable.attention_font_btn);
            mBtnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"关注成功，您可以报名了！",1).show();
                    SharedPreferencesUtil.saveObject(mExhibition, context, StringPools.SCAN_EXHIBITION_DATA);
                    mBtnSignup.setBackgroundResource(R.drawable.signup_font_btn);
                    mBtnSignup.setOnClickListener(new SignupClickListener(context,mExhibition.getExKey()));
                }
            });
        }
        mTxtTheme.setText(mExhibition.getName());
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
