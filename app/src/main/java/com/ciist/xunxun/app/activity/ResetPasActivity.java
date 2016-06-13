package com.ciist.xunxun.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ciist.xunxun.R;
import com.ciist.xunxun.app.util.Utils;
import com.ciist.xunxun.app.widget.CoustomCleanEditText;

import widget.CiistTitleView;

/**
 * Created by hw on 2016/5/5.
 */
public class ResetPasActivity extends Activity {

    private CoustomCleanEditText reset_newPas;
    private CoustomCleanEditText reset_againNewPas;
    private Intent mIntent;  //上一个Activity传下来的参数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        mIntent = getIntent();
        init();
    }

    /**
     * init view
     */
    private void init() {
        reset_newPas = (CoustomCleanEditText) findViewById(R.id.reset_newPas);
        reset_againNewPas = (CoustomCleanEditText) findViewById(R.id.reset_againNewPas);

        CiistTitleView reset_title = (CiistTitleView) findViewById(R.id.reset_title);
        reset_title.setOnLiftClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO : close current activity
                finish();
            }
        });

        Button reset_submit = (Button) findViewById(R.id.reset_submit);
        reset_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {    //提交修改密码
                //TODO : this's reset pasport after logic
                String newPassword = reset_newPas.getText().toString();
                String again = reset_againNewPas.getText().toString();
                String[] texts = new String[]{newPassword,again};
                if (Utils.editIsNull(ResetPasActivity.this,texts) && passwordIsSame(newPassword,again)){


                    //成功修改后便跳转
                    startActivity(new Intent(ResetPasActivity.this,ResetOKActivity.class));
                }



            }
        });

    }

    /**
     * 两次输入密码是否相同
     * @param one
     * @param two
     * @return
     */
    private boolean passwordIsSame(String one,String two){
        boolean is = true;
        if (!one.equals(two)){
            is = false;
            Toast.makeText(this,"两次输入密码不相同，请重新输入",Toast.LENGTH_SHORT).show();
        }
        return is;
    }
}
