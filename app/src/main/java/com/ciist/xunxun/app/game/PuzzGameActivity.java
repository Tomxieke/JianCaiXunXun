package com.ciist.xunxun.app.game;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ciist.xunxun.R;
import com.ciist.xunxun.app.activity.PaymentActivity;
import com.ciist.xunxun.app.widget.PuzzGameView;
import com.ciist.xunxun.app.widget.SystemBarTintManager;

/**
 * Created by hw on 2016/5/19.
 */
public class PuzzGameActivity extends Activity {

    private PuzzGameView puzzgame_view;
    private ImageView puzzgame_small_img;//原图
    private TextView puzzgame_time;//时间
    private TextView puzzgame_name;//用户
    private Button puzzgame_play;//开始游戏按钮
    private ProgressBar puzzgame_pro;//进度
    private Handler handler = new Handler();

    private boolean isPlay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        GameMusic.init(this);
//        GameMusic.setMusicSt(true);
//        GameMusic.setSoundSt(true);
        initSystemBar(this);//改变手机状态栏颜色

        //设定游戏难度
        Config.nandu = 3;

        //设置游戏布局宽度
        Config.metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(Config.metrics);

        //绑定布局
        setContentView(R.layout.activity_puzzgame);

        //绑定id
        puzzgame_view = (PuzzGameView) findViewById(R.id.puzzgame_view);
        puzzgame_small_img = (ImageView) findViewById(R.id.puzzgame_small_img);
        puzzgame_time = (TextView) findViewById(R.id.puzzgame_time);
        puzzgame_name = (TextView) findViewById(R.id.puzzgame_name);
        puzzgame_play = (Button) findViewById(R.id.puzzgame_play);
        puzzgame_pro = (ProgressBar) findViewById(R.id.puzzgame_pro);

        //设置默认游戏图片
        puzzgame_small_img.setBackgroundResource(R.mipmap.ciist_xunxun_game_testimg);

        //设定初始游戏进度
        puzzgame_pro.setMax(80);
        puzzgame_pro.setProgress(0);

        //设定用户
        puzzgame_name.setText("ID:" + Config.user);

        //得到用户体验权限
        isPlay = getIntent().getBooleanExtra("ISPLAY",false);

        isPlay = true;

        //游戏完成时的监听
        puzzgame_view.setOnPuzzOverListener(new PuzzGameView.OnPuzzOverListener() {

            @Override
            public void puzzOver() {
                finish();
            }

            @Override
            public void stopUi() {
                handler.removeCallbacks(runnable);
            }

            @Override
            public void setProgress(int p) {
                puzzgame_pro.setProgress(p * 10);
            }

        });


        puzzgame_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动线程，开始统计游戏时间
                if (isPlay){
                    puzzgame_view.isTouch = true;
                    Config.startTime = System.currentTimeMillis();
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, 50);
                    puzzgame_play.setBackgroundResource(R.mipmap.ciist_xunxun_game_play_p);
                    puzzgame_play.setClickable(false);
                }else{
                    new AlertDialog.Builder(PuzzGameActivity.this)
                            .setTitle("提示")
                            .setMessage("您需要购买参与活动资格后才能参与！" +
                                    "\r" +
                                    "确定支付吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(PuzzGameActivity.this,PaymentActivity.class));
                                    finish();
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                        }
                    }).create().show();
                }
            }
        });

    }

    //线程设定
    private Runnable runnable = new Runnable() {
        public void run () {
            update();
            handler.postDelayed(this,50);
        }
    };


    //刷新操作
    private void update(){

        Config.time = (int)((System.currentTimeMillis() - Config.startTime) / 1000);
        puzzgame_time.setText(Config.time + "");

    }


    /**
     * 改变状态栏颜色
     * @param activity
     */
    public static void initSystemBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.title_color);// 使用颜色资源
    }
    @TargetApi(19)
    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("确定退出游戏吗？" +
                            "\r" +
                            "退出游戏后将不能返回该游戏！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                }
            }).create().show();
//            GameMusic.pauseMusic();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
