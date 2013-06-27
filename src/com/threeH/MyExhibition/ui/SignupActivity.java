package com.threeH.MyExhibition.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.listener.TelephoneClickListener;
import com.threeH.MyExhibition.tools.MSYH;

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
        textViewTitle = (TextView) this.findViewById(R.id.exhibition_titlebar_textview_title);
        imageviewTelephone = (ImageView) this.findViewById(R.id.exhibition_titlebar_button_telephone);
        textViewCanzhan = (TextView) this.findViewById(R.id.signup_textview_canzhan);
        textViewCanhui = (TextView) this.findViewById(R.id.signup_textview_canhui);
    }

    @Override
    public void initdata() {
        exKey = getIntent().getStringExtra("exKey");
//        typeface = Typeface.createFromAsset(context.getAssets(),"fonts/msyh.ttf");
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
                       if(!("".equals(name)) && !("".equals(telephone)) && !("".equals(email))){
                           mController.getService().ExEnroll(exKey,token,name,telephone,email,type);
                           Dialog dialog = new AlertDialog.Builder(SignupActivity.this)
                                   .setMessage("提交成功").create();
                           dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                               @Override
                               public void onDismiss(DialogInterface dialog) {
                                   Intent intent = new Intent(SignupActivity.this, HomeOfTabActivity.class);
                                   intent.putExtra("exKey", exKey);
                                   intent.putExtra("currentTab",HomeOfTabActivity.TAB_SIGNUP);
                                   startActivity(intent);
                               }
                           });
                           dialog.show();
                       }else{
                           new AlertDialog.Builder(SignupActivity.this)
                                   .setMessage("姓名，联系方式，邮箱地址不能为空")
                                   .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface dialog, int which) {
                                       }
                                   }).show();
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