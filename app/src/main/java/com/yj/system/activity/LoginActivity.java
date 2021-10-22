package com.yj.system.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
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
 * created by on 2021/10/21
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-10-21-21:44
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText etUsername,etPassword;
    private Button btGo;

    private String username,password;
    private Call call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setListener();
    }

    private void initView() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btGo = findViewById(R.id.bt_go);
//        cv = findViewById(R.id.cv);
//        fab = findViewById(R.id.fab);
    }

    private void setListener() {
        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
    }

    private void Login() {

        username = etUsername.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if (username.isEmpty()) {
            Snackbar snackbar = Snackbar.make(btGo, "请输入用户名~", Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();

            return;
        }
        if (password.isEmpty()) {
            Snackbar snackbar = Snackbar.make(btGo, "请输入密码~", Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //开始登录
        doLoginSystem();
    }

    private void doLoginSystem() {
        LoadingDialog.showSimpleLD(LoginActivity.this,getString(R.string.loading));
        ClearableCookieJar cookie = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(LoginActivity.this));
        Log.i(TAG, "cookie============: " + cookie);
        //1.创建OkHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder().cookieJar(cookie).build();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)//传递键值对参数
                .add("pass", password)
                .build();
        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        final Request request = new Request.Builder()
                .url("http://417r050o47.zicp.vip/mangage/login.io")
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
                                    ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                                    Intent i2 = new Intent(LoginActivity.this,LoginSuccessActivity.class);
                                    i2.putExtra("username",username);
                                    startActivity(i2, oc2.toBundle());
                                    finish();
                                }
                            });

                        } else if (jsonObject.optString("state").equals("0") && jsonObject.optString("msg").equals("密码错误")) {
                            Snackbar snackbar = Snackbar.make(btGo, "用户名和密码不匹配！", Snackbar.LENGTH_LONG);
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
                Snackbar snackbar = Snackbar.make(btGo, "登录失败，服务器连接超时！", Snackbar.LENGTH_LONG);
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

    /**
     * 按下返回键
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        call.cancel();
        LoadingDialog.disDialog();
    }

}
