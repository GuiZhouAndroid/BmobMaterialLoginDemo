package com.yj.system.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yj.system.ui.Wave.Wave;
import com.yj.system.ui.Wave.WaveView;
import com.yj.system.utils.StatusBarUtils;
import com.yj.systemc.R;


/**
 * created by on 2021/10/21
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-10-21-21:56
 */
public class LoginSuccessActivity extends AppCompatActivity {
    private WaveView wave_view;//背景动画
    private TextView tv_username;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);
        Explode explode = new Explode();
        explode.setDuration(500);
        getWindow().setExitTransition(explode);
        getWindow().setEnterTransition(explode);

        StatusBarUtils.fullScreen(this);


        tv_username = findViewById(R.id.tv_username);
        tv_username.setText(getIntent().getStringExtra("username"));
        wave_view = (WaveView) findViewById(R.id.wave_view);//背景动画
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginSuccessActivity.this, fab, fab.getTransitionName());
                startActivity(new Intent(LoginSuccessActivity.this, PunchActivity.class), options.toBundle());
            }
        });
        initWave();
    }
    
    /**
     * 初始化背景动画
     */
    private void initWave() {

        wave_view.setOrientation(WaveView.Orientation.DOWN);
        int color = Color.parseColor("#55303F9F");
        Wave wave1 = new Wave(1080, 90, 10, 130, color);
        Wave wave2 = new Wave(1620, 78, -10, 140, color);
        Wave wave3 = new Wave(2080, 8, 8, 1500, color);
        wave_view.addWave(wave1);
        wave_view.addWave(wave2);
        wave_view.addWave(wave3);
    }
}
