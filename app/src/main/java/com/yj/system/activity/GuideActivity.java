package com.yj.system.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.yj.systemc.R;

/**
 * created by on 2021/10/23
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-10-23-22:16
 */
public class GuideActivity extends AppCompatActivity {
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LaunchOneSecondCountDown();//开启线程
    }

    /**
     * 1秒倒计时，进入视频引导页
     */
    private void LaunchOneSecondCountDown() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivityAnimInAndOut(GuideActivity.this,new Intent(GuideActivity.this, LoginActivity.class));
                finish();
            }
        }, 1000);
    }

    /**
     * 深入浅出：启动动画
     *
     * @param intent
     */
    public static void startActivityAnimInAndOut(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.launch_anim_fade_in, R.anim.launch_anim_fade_out);
    }

    /**
     * 屏蔽物理返回按钮
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 销毁线程
     * 清除Message和Runnable资源
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
