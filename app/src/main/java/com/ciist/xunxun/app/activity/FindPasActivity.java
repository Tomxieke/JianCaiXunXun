package com.ciist.xunxun.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ciist.xunxun.R;
import com.ciist.xunxun.app.util.Utils;
import com.ciist.xunxun.app.widget.CoustomCleanEditText;

import widget.CiistTitleView;

/**
 * Created by hw on 2016/5/5.
 */
public class FindPasActivity extends Activity {

    private CoustomCleanEditText findPasport_user;
    private CoustomCleanEditText findPasport_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpasport);
        init();
    }

    /**
     * init view
     */
    private void init() {
        findPasport_user = (CoustomCleanEditText) findViewById(R.id.findPasport_user);
        findPasport_code = (CoustomCleanEditText) findViewById(R.id.findPasport_code);

        CiistTitleView findPasport_title = (CiistTitleView) findViewById(R.id.findPasport_title);
        findPasport_title.setOnLiftClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO : close current activity
                finish();
            }
        });

        TextView findPasport_getCode = (TextView) findViewById(R.id.findPasport_getCode);
        findPasport_getCode.setOnClickListener(new View.OnClickListener() { //获取验证码

            @Override
            public void onClick(View v) {
                //TODO : this's get verification code
                String phoneNum = findPasport_user.getText().toString();
                if (Utils.isPhoneNum(FindPasActivity.this,phoneNum)){
                    Toast.makeText(FindPasActivity.this," is phoneNumber ",Toast.LENGTH_SHORT).show();

                }
            }
        });

        Button findPasport_next = (Button) findViewById(R.id.findPasport_next);
        findPasport_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {               //下一步
                //TODO : click enter next activity
                String phoneNum = findPasport_user.getText().toString();
                String code = findPasport_code.getText().toString();
                String[] editTexts = new String[]{phoneNum,code};
                if (Utils.editIsNull(FindPasActivity.this,editTexts)){

                    Intent intent = new Intent(FindPasActivity.this,ResetPasActivity.class);
                    intent.putExtra(Utils.PHONE_NUM,phoneNum);
                    intent.putExtra(Utils.CODE,code);
                    startActivity(intent);


                }

            }
        });
    }
}
