package com.ciist.xunxun.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.ciist.xunxun.R;
import com.ciist.xunxun.app.util.Utils;
import com.ciist.xunxun.app.widget.CoustomCleanEditText;

import widget.CiistTitleView;

/**
 * Created by hw on 2016/5/5.
 */
public class RegisterActivity extends Activity {

    private CoustomCleanEditText register_user;
    private CoustomCleanEditText register_code;
    private CoustomCleanEditText register_pas;
    private CoustomCleanEditText register_name;
    private CheckBox register_cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    /**
     * init view
     */
    private void init() {
        register_user = (CoustomCleanEditText) findViewById(R.id.register_user);
        register_code = (CoustomCleanEditText) findViewById(R.id.register_code);
        register_pas = (CoustomCleanEditText) findViewById(R.id.register_pas);
        register_name = (CoustomCleanEditText) findViewById(R.id.register_name);
        register_cb = (CheckBox) findViewById(R.id.register_cb);

        CiistTitleView register_title = (CiistTitleView) findViewById(R.id.register_title);
        register_title.setOnLiftClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO : close current activity
                finish();
            }
        });

        TextView register_getCode = (TextView) findViewById(R.id.register_getCode);  //获取手机验证码
        register_getCode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO : this's get verification code
                String phoneNum = register_user.getText().toString();
                if (Utils.isPhoneNum(RegisterActivity.this,phoneNum)){
                    Toast.makeText(RegisterActivity.this,"是手机号码，可以去获取验证码了",Toast.LENGTH_SHORT).show();


                }
            }
        });

        Button register_registering = (Button) findViewById(R.id.register_registering);
        register_registering.setOnClickListener(new View.OnClickListener() {  //注册按钮

            @Override
            public void onClick(View v) {
                //TODO : this's register button
                String phoneNum = register_user.getText().toString();
                String code = register_code.getText().toString();
                String password = register_pas.getText().toString();
                String name = register_name.getText().toString();
                String[] editTexts = new String[]{phoneNum,code,password,name};
                if (Utils.editIsNull(RegisterActivity.this,editTexts)){   //判断信息是否都输入了


                    //若果注册成功便跳转的页面
                    startActivity(new Intent(RegisterActivity.this,RegisterOKActivity.class));
                }


            }
       });


        TextView register_agreement = (TextView) findViewById(R.id.register_agreement);
        register_agreement.setOnClickListener(new View.OnClickListener() {  //注册协议

            @Override
            public void onClick(View v) {
                //TODO : click enter register agreement activity
            }
        });
    }





}
