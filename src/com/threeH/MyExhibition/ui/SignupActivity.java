package com.threeH.MyExhibition.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.threeH.MyExhibition.R;

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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        buttonSingUp = (Button) this.findViewById(R.id.confirm_btn);
    }

    @Override
    public void initdata() {
        exKey = getIntent().getStringExtra("exKey");
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
                       mController.getService().ExEnroll(exKey,"pjqAndroid",name,telephone,email);
                   }catch (Exception e){
Log.e("data",e.getMessage());
                   }
               }
           });
    }
}