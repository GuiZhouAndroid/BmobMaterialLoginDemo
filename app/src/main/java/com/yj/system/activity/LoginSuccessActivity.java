package com.yj.system.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.yj.system.ui.Wave.Wave;
import com.yj.system.ui.Wave.WaveView;
import com.yj.system.utils.StatusBarUtils;
import com.yj.system.utils.dialog.LoadingDialog;
import com.yj.systemc.R;

import java.util.Date;


/**
 * created by on 2021/10/21
 * 描述：登录成功界面
 *
 * @author ZSAndroid
 * @create 2021-10-21-21:56
 */
public class LoginSuccessActivity extends AppCompatActivity implements View.OnClickListener {
    private WaveView wave_view;//背景动画
    private TextView tv_username, tv_yj; //叶静毕设Lable标签+跑马灯登录用户名
    private FloatingActionButton fab;//打卡动画按钮
    private long firstTime;//3s再按一次返回

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);
        initData();//准备数据
        initView();//初始化View
        setListener();//设置监听事件
        initWave();

    }

    /**
     * 页面加载+准备数据
     */
    private void initData() {
        //设置沉浸式状态栏
        StatusBarUtils.fullScreen(this);
        Explode explode = new Explode();
        explode.setDuration(500);
        getWindow().setExitTransition(explode);
        getWindow().setEnterTransition(explode);
    }


    private void initView() {
        tv_username = findViewById(R.id.tv_username);//跑马灯登录用户名
        wave_view = (WaveView) findViewById(R.id.wave_view);//背景动画
        fab = findViewById(R.id.fab);
        tv_yj = findViewById(R.id.tv_yj);
        //优先级显示，作用--->不被背景动画遮挡，显示最顶层
        tv_yj.bringToFront();
        tv_username.bringToFront();
        //设置友好时间提示
        getDate(tv_username);
    }

    /**
     * 设置监听事件
     */
    private void setListener() {
        fab.setOnClickListener(this);//给浮动按钮(打卡)，设置监听
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

    /**
     * 设置控件点击事件的执行逻辑
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab: //点击打卡按钮动画过度打卡界面
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                //动画过渡开始
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginSuccessActivity.this, fab, fab.getTransitionName());
                //同时执行跳转
                startActivity(new Intent(LoginSuccessActivity.this, PunchActivity.class), options.toBundle());
                break;
        }
    }

    /**
     * 判断当前时间处于那个时间
     *
     * @param tv
     */
    private void getDate(TextView tv) {
        String loginUserName = getIntent().getStringExtra("username");
        Date d = new Date();
        if (d.getHours() < 4) {
            tv.setText("                 早点休息哟~，" + loginUserName + "                 ");
        } else if (d.getHours() < 11) {
            tv.setText("                 早上好，" + loginUserName + "                 ");
        } else if (d.getHours() < 14) {
            tv.setText("                 中午好，" + loginUserName + "                 ");
        } else if (d.getHours() < 18) {
            tv.setText("                 下午好，" + loginUserName + "                 ");
        } else if (d.getHours() < 24) {
            tv.setText("                 晚上好，" + loginUserName + "                 ");
        }
    }



    /**
     * 防触碰处理
     * 再按一次退出程序
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 3000) {
                    Snackbar snackbar = Snackbar.make(wave_view, "再按一次退出程序！", Snackbar.LENGTH_LONG);
                    //设置Snackbar上提示的字体颜色
                    setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                    snackbar.show();
                    firstTime = secondTime;
                    return true;
                } else {
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void setSnackBarMessageTextColor(Snackbar snackbar, int color) {
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }
}
