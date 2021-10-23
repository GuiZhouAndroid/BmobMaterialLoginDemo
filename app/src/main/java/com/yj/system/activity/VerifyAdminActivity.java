package com.yj.system.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yj.system.ui.Wave.PowerfulEditText;
import com.yj.system.utils.StatusBarUtils;
import com.yj.systemc.R;

/**
 * created by on 2021/10/22
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-10-22-20:48
 */
public class VerifyAdminActivity extends AppCompatActivity {
    PowerfulEditText verify_query;//动画EditText
    Button btn_star_verify;//验证按钮
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_admin);
        //设置沉浸式状态栏
        StatusBarUtils.fullScreen(this);
        Explode explode = new Explode();
        explode.setDuration(500);
        getWindow().setExitTransition(explode);
        getWindow().setEnterTransition(explode);
        verify_query = findViewById(R.id.verify_query);
        btn_star_verify = findViewById(R.id.btn_star_verify);
        btn_star_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strContent =verify_query.getText().toString().trim();
                if (strContent.equals("叶静")){
                    startActivity(new Intent(VerifyAdminActivity.this,SuperLoginActivity.class));
                    finish();
                }else {
                    verify_query.startShakeAnimation();
                    Snackbar snackbar = Snackbar.make(btn_star_verify, "验证码不正确", Snackbar.LENGTH_LONG);
                    //设置Snackbar上提示的字体颜色
                    setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                    snackbar.show();
                }
            }
        });
    }

    public void setSnackBarMessageTextColor(Snackbar snackbar, int color) {
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }
}
