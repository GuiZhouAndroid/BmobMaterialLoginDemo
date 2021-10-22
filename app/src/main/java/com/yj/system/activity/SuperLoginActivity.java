package com.yj.system.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.yj.system.utils.StatusBarUtils;
import com.yj.system.utils.dialog.LoadingDialog;
import com.yj.systemc.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * created by on 2021/10/22
 * 描述：超级管理员登录
 *
 * @author ZSAndroid
 * @create 2021-10-22-19:37
 */
public class SuperLoginActivity extends AppCompatActivity {
    private static final String TAG = "SuperLoginActivity";
    private CardView cvAdd;
    private FloatingActionButton fab_super;
    private EditText et_admin_username,et_admin_password;
    private Button bt_go_admin;
    private String username,password;
    private Call call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_login);
        StatusBarUtils.fullScreen(this);
        ShowEnterAnimation();
        initView();
        fab_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
    }

    private void initView() {
        fab_super = findViewById(R.id.fab_super);
        cvAdd = findViewById(R.id.cv_add);
        et_admin_username = findViewById(R.id.et_admin_username);
        et_admin_password = findViewById(R.id.et_admin_password);
        bt_go_admin = findViewById(R.id.bt_go_admin);

        bt_go_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
    }

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth()/2,0, fab_super.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd,cvAdd.getWidth()/2,0, cvAdd.getHeight(), fab_super.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab_super.setImageResource(R.drawable.plus);
                SuperLoginActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }
    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    private void Login() {
        username = et_admin_username.getText().toString().trim();
        password = et_admin_password.getText().toString().trim();
        if (username.isEmpty()) {
            Snackbar snackbar = Snackbar.make(bt_go_admin, "请输入用户名~", Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();

            return;
        }
        if (password.isEmpty()) {
            Snackbar snackbar = Snackbar.make(bt_go_admin, "请输入密码~", Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //开始登录
        doLoginSystem();
    }

    private void doLoginSystem() {
        LoadingDialog.showSimpleLD(SuperLoginActivity.this,getString(R.string.loading));
        ClearableCookieJar cookie = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(SuperLoginActivity.this));
        Log.i(TAG, "cookie============: " + cookie);
        //1.创建OkHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder().cookieJar(cookie).build();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)//传递键值对参数
                .add("pass", password)
                .build();
        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        final Request request = new Request.Builder()
                .url("http://192.168.157.29:8083/mangage/login.io")
                .post(requestBody)
                .build();

        //new call
        call = mOkHttpClient.newCall(request);

        JSONObject jsonObject = null;
        //5.请求加入调度,重写回调方法，异步方式请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        Log.i(TAG, "结果===: " + res);
                        if (jsonObject.optString("state").equals("1") && jsonObject.optString("msg").equals("登陆请求成功")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LoadingDialog.closeSimpleLD();
                                    Explode explode = new Explode();
                                    explode.setDuration(500);
                                    getWindow().setExitTransition(explode);
                                    getWindow().setEnterTransition(explode);
                                    ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(SuperLoginActivity.this);
                                    Intent i2 = new Intent(SuperLoginActivity.this,LoginSuccessActivity.class);
                                    i2.putExtra("username",username);
                                    startActivity(i2, oc2.toBundle());
                                    finish();
                                }
                            });

                        } else if (jsonObject.optString("state").equals("0") && jsonObject.optString("msg").equals("用户不存在")) {
                            Snackbar snackbar = Snackbar.make(bt_go_admin, "用户名和密码不匹配！", Snackbar.LENGTH_LONG);
                            //设置Snackbar上提示的字体颜色
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            LoadingDialog.closeSimpleLD();


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    throw new IOException("发生错误:" + response.body().string());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Snackbar snackbar = Snackbar.make(bt_go_admin, "登录失败，服务器连接超时！", Snackbar.LENGTH_LONG);
                //设置Snackbar上提示的字体颜色
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                LoadingDialog.closeSimpleLD();
            }
        });
    }


    public void setSnackBarMessageTextColor(Snackbar snackbar, int color) {
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }
}
