package com.yj.system.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.yj.system.utils.StatusBarUtils;
import com.yj.system.utils.dialog.LoadingDialog;
import com.yj.system.utils.singlepicker.PickerBean.PickerItem;
import com.yj.system.utils.singlepicker.SinglePicker;
import com.yj.systemc.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
 * @create 2021-10-21-22:51
 */
public class PunchActivity extends AppCompatActivity {
    private static final String TAG = "PunchActivity";
    private LinearLayout ll_choose_dept;
    private FloatingActionButton fab;
    private CardView cvAdd;
    private TextView tv_now_time, tv_dept;
    private EditText et_staff_name;
    private Button btn_play_card;
    private String strStaffName,strDeptValue;//员工名+部门名

    private Call call;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    //获取到系统当前时间 long类型
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时:mm分:ss秒");
                    tv_now_time.setText("当前打卡时间：" + dateFormat.format(date));
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punch);
        new TimeThread().start();
        StatusBarUtils.fullScreen(this);
        initView();
        setListener();

    }


    private void initView() {
        fab = findViewById(R.id.fab);
        cvAdd = findViewById(R.id.cv_add);
        tv_now_time = findViewById(R.id.tv_now_time);
        et_staff_name = findViewById(R.id.et_staff_name);
        ll_choose_dept = findViewById(R.id.ll_choose_dept);
        tv_dept = findViewById(R.id.tv_dept);
        btn_play_card = findViewById(R.id.btn_play_card);
    }

    private void setListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });

        ll_choose_dept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<PickerItem> DeclareHradeData = new ArrayList<>();
                DeclareHradeData.add(new PickerItem(1, "人力资源部"));
                DeclareHradeData.add(new PickerItem(2, "代表处"));
                DeclareHradeData.add(new PickerItem(3, "企划部"));
                DeclareHradeData.add(new PickerItem(4, "供应链管理部"));
                DeclareHradeData.add(new PickerItem(5, "后勤管理部"));
                DeclareHradeData.add(new PickerItem(6, "市场部"));
                DeclareHradeData.add(new PickerItem(7, "技术研发部"));
                DeclareHradeData.add(new PickerItem(8, "测试部"));
                DeclareHradeData.add(new PickerItem(9, "维护安装部"));
                DeclareHradeData.add(new PickerItem(10, "财务部"));
                DeclareHradeData.add(new PickerItem(11, "销售部"));
                SinglePicker<PickerItem> picker = new SinglePicker<>(PunchActivity.this, DeclareHradeData);
                picker.setCanceledOnTouchOutside(false);
                picker.setCycleDisable(true);//禁用循环选择
                picker.setSelectedIndex(1);//默认选择index
                picker.setTitleTextColor(0xFFFFFF);//标题字体颜色
                picker.setTitleTextSize(20);//标题字体大小
                picker.setCancelTextColor(0xFFEE0000);//设置取消的字体颜色
                picker.setCancelTextSize(16);//取消字体大小
                picker.setSubmitTextColor(0xFFEE0000);//设置确定的字体颜色
                picker.setSubmitTextSize(16);//确定字体大小
                //设置Item监听，并把选择的值填入资料中
                picker.setOnItemPickListener(new SinglePicker.OnItemPickListener<PickerItem>() {
                    @Override
                    public void onItemPicked(int index, PickerItem item) {
                        switch (index) {//设置是否二考
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                            case 10:
                                tv_dept.setText(item.getName());
                                break;
                        }
                    }
                });
                picker.show();
            }
        });

        btn_play_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strStaffName = et_staff_name.getText().toString().trim();
                Log.i(TAG, "员工名称-----: " + strStaffName);
                strDeptValue = tv_dept.getText().toString().trim();
                Log.i(TAG, "部门名称-----: " + strDeptValue);
                if (strStaffName.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(btn_play_card, "请输入员工名字~", Snackbar.LENGTH_LONG);
                    //设置Snackbar上提示的字体颜色
                    setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                    snackbar.show();
                    return;
                }
                if (strDeptValue.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(btn_play_card, "请选择您所属的部门类别", Snackbar.LENGTH_LONG);
                    //设置Snackbar上提示的字体颜色
                    setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                    snackbar.show();
                    return;
                }
                //开始打卡
                doPlayCard();
            }
        });
    }

    /**
     * 开始打卡
     */
    private void doPlayCard() {

        LoadingDialog.showSimpleLD(PunchActivity.this, getString(R.string.loading));
        ClearableCookieJar cookie = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(PunchActivity.this));
        Log.i(TAG, "cookie============: " + cookie);
        //1.创建OkHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder().cookieJar(cookie).build();
        RequestBody requestBody = new FormBody.Builder()
                .add("empName", String.valueOf(et_staff_name))//传递键值对参数
                .add("dept", String.valueOf(tv_dept))
                .build();
        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        final Request request = new Request.Builder()
                .url("http://192.168.157.29:8083/work/addPunchInfo")
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
                        if (jsonObject.optString("state").equals("0") && jsonObject.optString("msg").equals("本次打卡已结束,下次要提前哟~！")) {
                            Snackbar snackbar = Snackbar.make(btn_play_card, jsonObject.optString("msg")+","+jsonObject.optString("data"), Snackbar.LENGTH_LONG);
                            //设置Snackbar上提示的字体颜色
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            LoadingDialog.closeSimpleLD();
                        } else if (jsonObject.optString("state").equals("0") && jsonObject.optString("msg").equals("管理员未启用打卡功能")) {
                            Snackbar snackbar = Snackbar.make(btn_play_card, jsonObject.optString("msg")+","+jsonObject.optString("data"), Snackbar.LENGTH_LONG);
                            //设置Snackbar上提示的字体颜色
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            LoadingDialog.closeSimpleLD();

                        } else if (jsonObject.optString("state").equals("0") && jsonObject.optString("msg").equals("早上打卡失败")) {
                            Snackbar snackbar = Snackbar.make(btn_play_card, jsonObject.optString("msg")+","+jsonObject.optString("data"), Snackbar.LENGTH_LONG);
                            //设置Snackbar上提示的字体颜色
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            LoadingDialog.closeSimpleLD();

                        }else if (jsonObject.optString("state").equals("0") && jsonObject.optString("msg").equals("早上中午失败")){
                            Snackbar snackbar = Snackbar.make(btn_play_card, jsonObject.optString("msg")+","+jsonObject.optString("data"), Snackbar.LENGTH_LONG);
                            //设置Snackbar上提示的字体颜色
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            LoadingDialog.closeSimpleLD();
                        }else if (jsonObject.optString("state").equals("0") && jsonObject.optString("msg").equals("下午中午失败")){
                            Snackbar snackbar = Snackbar.make(btn_play_card, jsonObject.optString("msg")+","+jsonObject.optString("data"), Snackbar.LENGTH_LONG);
                            //设置Snackbar上提示的字体颜色
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            LoadingDialog.closeSimpleLD();
                        }else if (jsonObject.optString("state").equals("0") && jsonObject.optString("msg").equals("晚上中午失败")){
                            Snackbar snackbar = Snackbar.make(btn_play_card, jsonObject.optString("msg")+","+jsonObject.optString("data"), Snackbar.LENGTH_LONG);
                            //设置Snackbar上提示的字体颜色
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            LoadingDialog.closeSimpleLD();
                        }else if (jsonObject.optString("state").equals("1") && jsonObject.optString("msg").equals("早上成功打卡")){
                            Toast.makeText(PunchActivity.this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LoadingDialog.closeSimpleLD();
                                    Explode explode = new Explode();
                                    explode.setDuration(500);
                                    getWindow().setExitTransition(explode);
                                    getWindow().setEnterTransition(explode);
                                    ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(PunchActivity.this);
                                    Intent i2 = new Intent(PunchActivity.this, LoginSuccessActivity.class);
                                    startActivity(i2, oc2.toBundle());
                                    finish();
                                }
                            });
                        }else if (jsonObject.optString("state").equals("1") && jsonObject.optString("msg").equals("中午成功打卡")){
                            Toast.makeText(PunchActivity.this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LoadingDialog.closeSimpleLD();
                                    Explode explode = new Explode();
                                    explode.setDuration(500);
                                    getWindow().setExitTransition(explode);
                                    getWindow().setEnterTransition(explode);
                                    ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(PunchActivity.this);
                                    Intent i2 = new Intent(PunchActivity.this, LoginSuccessActivity.class);
                                    startActivity(i2, oc2.toBundle());
                                    finish();
                                }
                            });
                        }else if (jsonObject.optString("state").equals("1") && jsonObject.optString("msg").equals("下午成功打卡")){
                            Toast.makeText(PunchActivity.this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LoadingDialog.closeSimpleLD();
                                    Explode explode = new Explode();
                                    explode.setDuration(500);
                                    getWindow().setExitTransition(explode);
                                    getWindow().setEnterTransition(explode);
                                    ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(PunchActivity.this);
                                    Intent i2 = new Intent(PunchActivity.this, LoginSuccessActivity.class);
                                    startActivity(i2, oc2.toBundle());
                                    finish();
                                }
                            });
                        }else if (jsonObject.optString("state").equals("1") && jsonObject.optString("msg").equals("晚上成功打卡")){
                            Toast.makeText(PunchActivity.this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LoadingDialog.closeSimpleLD();
                                    Explode explode = new Explode();
                                    explode.setDuration(500);
                                    getWindow().setExitTransition(explode);
                                    getWindow().setEnterTransition(explode);
                                    ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(PunchActivity.this);
                                    Intent i2 = new Intent(PunchActivity.this, LoginSuccessActivity.class);
                                    startActivity(i2, oc2.toBundle());
                                    finish();
                                }
                            });
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
                Snackbar snackbar = Snackbar.make(btn_play_card, "登录失败，服务器连接超时！", Snackbar.LENGTH_LONG);
                //设置Snackbar上提示的字体颜色
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                LoadingDialog.closeSimpleLD();
            }
        });

    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                PunchActivity.super.onBackPressed();
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

    /**
     * 开启一个线程，每个一秒钟更新时间
     */
    public class TimeThread extends Thread {
        //重写run方法
        @Override
        public void run() {
            super.run();
            // do-while  一 什么什么 就
            do {
                try {
                    //每隔一秒 发送一次消息
                    Thread.sleep(1000);
                    Message msg = new Message();
                    //消息内容 为MSG_ONE
                    msg.what = 100;
                    //发送
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    public void setSnackBarMessageTextColor(Snackbar snackbar, int color) {
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }
}
