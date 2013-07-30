package com.threeH.MyExhibition.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.listener.TelephoneClickListener;
import com.threeH.MyExhibition.tools.MSYH;
import com.threeH.MyExhibition.tools.RegexUtils;
import com.threeH.MyExhibition.tools.SharedPreferencesUtil;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-6-20
 * Time: 下午1:45
 * To change this template use File | Settings | File Templates.
 */
public class SignupActivity extends  BaseActivity implements ActivityInterface{
    private EditText editTextName;
    private EditText editTextTelephone;
    private EditText editTextEmail;
    private String name;
    private String telephone;
    private String email;
    private Button buttonSingUp;
    private String exKey;
    private String type = "A";
    private ImageView imageViewAttendee,imageviewExhibitor,imageviewTelephone;
    Typeface typeface;
    private TextView textViewTitle;
    private TextView textViewCanzhan,textViewCanhui;
    private TextView textViewPhoneCheck,textViewEmailCheck,textViewNameCheck;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.signup_page);
        findView();
        initdata();
        addAction();
    }

    @Override
    public void findView() {
        editTextName = (EditText) this.findViewById(R.id.user_name);
        editTextTelephone = (EditText) this.findViewById(R.id.phone_nummber);
        editTextEmail = (EditText) this.findViewById(R.id.email_address);
        buttonSingUp = (Button) this.findViewById(R.id.submit_btn);
        imageViewAttendee = (ImageView) this.findViewById(R.id.signup_imageview_canhui);
        imageviewExhibitor = (ImageView) this.findViewById(R.id.signup_imageview_canzhan);
        textViewTitle = (TextView) this.findViewById(R.id.exhibition_titlebar_txt_title);
        imageviewTelephone = (ImageView) this.findViewById(R.id.exhibition_titlebar_btn_telephone);
        textViewCanzhan = (TextView) this.findViewById(R.id.signup_textview_canzhan);
        textViewCanhui = (TextView) this.findViewById(R.id.signup_textview_canhui);
        textViewPhoneCheck = (TextView) this.findViewById(R.id.signup_tv_phone);
        textViewEmailCheck = (TextView) this.findViewById(R.id.signup_tv_email);
        textViewNameCheck = (TextView) this.findViewById(R.id.signup_tv_name);
    }

    @Override
    public void initdata() {
        exKey = getIntent().getStringExtra("exKey");
Log.i("data",exKey);
        typeface = MSYH.getInstance(context.getApplicationContext()).getNormal();
    }

    @Override
    public void addAction() {
           buttonSingUp.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   try{
                       name = editTextName.getText().toString();
                       telephone = editTextTelephone.getText().toString();
                       email = editTextEmail.getText().toString();
                       if(name == null || name.equals("")){
                           textViewNameCheck.setVisibility(View.VISIBLE);
                       }else if(!RegexUtils.checkMobile(telephone)){
                           textViewPhoneCheck.setVisibility(View.VISIBLE);
                       }else if(!RegexUtils.verifyEmail(email)){
                           textViewEmailCheck.setVisibility(View.VISIBLE);
                       }else{
                           mController.getService().ExEnroll(exKey,token,name,telephone,email,type);
                           Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                           SharedPreferencesUtil.removeObject(exKey,context, StringPools.SCAN_EXHIBITION_DATA);
                           intent.putExtra("exKey", exKey);
                           startActivity(intent);
                       }
                   }catch (Exception e){
                   }
               }
           });
        TypeClickListener typeClickListener = new TypeClickListener();
        imageViewAttendee.setOnClickListener(typeClickListener);
        imageviewExhibitor.setOnClickListener(typeClickListener);
        textViewCanzhan.setOnClickListener(typeClickListener);
        textViewCanhui.setOnClickListener(typeClickListener);
        imageviewTelephone.setOnClickListener(new TelephoneClickListener(context,tel_nummber));
        textViewTitle.setTypeface(typeface);
        textViewTitle.setText("申请报名");

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textViewNameCheck.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextTelephone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textViewPhoneCheck.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textViewEmailCheck.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    class  TypeClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.signup_textview_canzhan:
                case R.id.signup_imageview_canzhan:
                    imageviewExhibitor.setBackgroundResource(R.drawable.yuan_focus);
                    imageViewAttendee.setBackgroundResource(R.drawable.yuan_unfocus);
                    type ="E";
                    break;
                case R.id.signup_textview_canhui:
                case R.id.signup_imageview_canhui:
                    imageviewExhibitor.setBackgroundResource(R.drawable.yuan_unfocus);
                    imageViewAttendee.setBackgroundResource(R.drawable.yuan_focus);
                    type ="A";
                    break;
            }
        }
    }

}