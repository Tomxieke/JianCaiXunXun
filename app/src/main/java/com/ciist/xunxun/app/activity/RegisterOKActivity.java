package com.ciist.xunxun.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ciist.xunxun.R;

/**
 * Created by hw on 2016/5/5.
 */
public class RegisterOKActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ok);

        Button reset_OK = (Button) findViewById(R.id.register_OK);
        reset_OK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterOKActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
