package arroon.android.login.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import arroon.android.login.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";

    private EditText mUsername;
    private EditText mPassword;
    private Button mDoLogin;
    public RelativeLayout mRl_show;
    private String username;
    private String password;
    //    private FloatingActionButton mGoRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView();
        setListener();
    }

    private void initView() {
        mUsername = findViewById(R.id.et_username);
        mPassword = findViewById(R.id.et_password);
        mDoLogin = findViewById(R.id.bt_go);
    }

    private void setListener() {
        mDoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = mUsername.getText().toString().trim();
                password = mPassword.getText().toString().trim();
                if (username.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(mDoLogin, "请输入用户名~", Snackbar.LENGTH_LONG);
                    //设置Snackbar上提示的字体颜色
                    setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                    snackbar.show();
                    return;
                }
                if (password.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(mDoLogin, "请输入密码~", Snackbar.LENGTH_LONG);
                    //设置Snackbar上提示的字体颜色
                    setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                    snackbar.show();
                    return;
                }
                //开始登录
                doLoginSystem();
            }
        });
    }

    private void doLoginSystem() {
        ClearableCookieJar cookie = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Login.this));
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
        Call call = mOkHttpClient.newCall(request);

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
                            startActivity(new Intent(Login.this, User.class));
                            finish();
                        } else if (jsonObject.optString("state").equals("0") && jsonObject.optString("msg").equals("用户不存在")) {
                            Snackbar snackbar = Snackbar.make(mDoLogin, "用户名和密码不匹配！", Snackbar.LENGTH_LONG);
                            //设置Snackbar上提示的字体颜色
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
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
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });
    }

    public void setSnackBarMessageTextColor(Snackbar snackbar, int color) {
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }
}
