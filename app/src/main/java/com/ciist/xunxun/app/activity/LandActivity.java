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
public class LandActivity extends Activity {

    private CoustomCleanEditText land_user;
    private CoustomCleanEditText land_pas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land);
        init();
    }

    /**
     * init view
     */
    private void init() {
        land_user = (CoustomCleanEditText) findViewById(R.id.land_user);
        land_pas = (CoustomCleanEditText) findViewById(R.id.land_pas);

        CiistTitleView land_title = (CiistTitleView) findViewById(R.id.land_title);
        land_title.setOnLiftClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO :off current activity
                finish();
            }
        });

        land_title.setOnRightClickListener(new View.OnClickListener() {  //注册

            @Override
            public void onClick(View v) {
                //TODO :enter register activity
                startActivity(new Intent(LandActivity.this,RegisterActivity.class));
            }
        });


        Button land_landing = (Button) findViewById(R.id.land_landing);  //登录
        land_landing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO : click landing button for logic???
                String user = land_user.getText().toString();
                String passwore = land_pas.getText().toString();
                String editTexts[] = new String[]{user,passwore};
                if (Utils.editIsNull(LandActivity.this,editTexts)){
                    //登录请求，获取信息
                    Toast.makeText(LandActivity.this,"信息填写完整,可以执行登录请求",Toast.LENGTH_SHORT).show();
                }
            }
        });


        TextView land_resetPas = (TextView) findViewById(R.id.land_resetPas);  //忘记密码
        land_resetPas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO : click enter reset pasport activity
                startActivity(new Intent(LandActivity.this,FindPasActivity.class));
            }
        });

    }



}
