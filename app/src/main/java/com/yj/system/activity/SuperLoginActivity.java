package com.yj.system.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.defaults.logger.Logger;
import top.defaults.view.DateTimePickerView;

/**
 * created by on 2021/10/22
 * 描述：超级管理员登录
 *
 * @author ZSAndroid
 * @create 2021-10-22-19:37
 */
public class SuperLoginActivity extends AppCompatActivity {
    private static final String TAG = "SuperLoginActivity";
    private CardView cvAdd;//卡片布局，用于动画过渡
    private FloatingActionButton fab_super;//浮动按钮
    private Button bt_set_end_time;//立即生效
    private Call call;//okhttp3网络请求回调
    private DateTimePickerView dateTimePickerView;//填写资料的时间日期选择器
    private TextView tv_end_time;//选择时间
    private String year,month,day,hours,minute;
    private int type = DateTimePickerView.TYPE_DATE_HOUR_MINUTE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_login);
        StatusBarUtils.fullScreen(this);
        ShowEnterAnimation();
        initView();
        initPickerTime();//滑动选择时间器

    }

    private void initView() {
        cvAdd = findViewById(R.id.cv_add);
        fab_super = findViewById(R.id.fab_super);
        tv_end_time = findViewById(R.id.tv_end_time);
        dateTimePickerView = findViewById(R.id.dateTimePickerView);
        bt_set_end_time = findViewById(R.id.bt_set_end_time);
        bt_set_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tv_end_time.getText().toString().trim().isEmpty()){
                    doSetData();//立即设置生效
                }else {
                    Snackbar snackbar = Snackbar.make(bt_set_end_time, "请重新选择打卡时间", Snackbar.LENGTH_LONG);
                    //设置Snackbar上提示的字体颜色
                    setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                    snackbar.show();
                }
            }
        });

        fab_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });

    }
    /**
     * 选择时间
     */
    private void initPickerTime() {
        dateTimePickerView.setCurved(true);
        dateTimePickerView.setType(type);
        if (dateTimePickerView.getTimePickerView() != null) {
            dateTimePickerView.getTimePickerView().setTextColor(Color.MAGENTA);
        }
        dateTimePickerView.setStartDate(new GregorianCalendar(2017, 1, 27, 21, 30));
        dateTimePickerView.setSelectedDate(Calendar.getInstance());
        dateTimePickerView.setEndDate(new GregorianCalendar(2099, 12, 31));
        dateTimePickerView.setOnSelectedDateChangedListener(date -> {
            String dateString = getDateString(date);//获取动态滑动显示的时间
            tv_end_time.setText(dateString);//设置时间显示到TextView
            //准备网络请求参数
            year = dateString.substring(0,4);//取年的数字
            month = dateString.substring(5,7);//取月的数字
            day = dateString.substring(8,10);//取日的数字
            hours = dateString.substring(11,13);//取小时的数字
            minute = dateString.substring(14,16);  //取分钟的数字
            Logger.d("new date: %s", dateString);
        });

        tv_end_time.setText(getDateString(dateTimePickerView.getSelectedDate()));
    }

    private String getDateString(Calendar date) {
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);
        int hour = date.get(Calendar.HOUR_OF_DAY);
        int minute = date.get(Calendar.MINUTE);
        return String.format(Locale.getDefault(), "%d年%02d月%02d日%02d时%02d分", year, month + 1, dayOfMonth, hour, minute);
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


    private void doSetData() {
        LoadingDialog.showSimpleLD(SuperLoginActivity.this,getString(R.string.loading));
        ClearableCookieJar cookie = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(SuperLoginActivity.this));
        Log.i(TAG, "cookie============: " + cookie);
        //1.创建OkHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder().cookieJar(cookie).build();
        RequestBody requestBody = new FormBody.Builder()
                .add("endYear", year)//传递键值对参数
                .add("endMonth", month)
                .add("endDay", day)//传递键值对参数
                .add("endHours", hours)
                .add("endMinute", minute)//传递键值对参数
                .add("endSecond", "00")
                .build();
        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        final Request request = new Request.Builder()
                .url("http://192.168.157.29:8083/work/setPlayCardEndTime")
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
                        if (jsonObject.optString("state").equals("1") && jsonObject.optString("msg").equals("设置结束打卡时间成功")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    animateRevealClose();
                                    Toast.makeText(SuperLoginActivity.this, jsonObject.optString("msg")+"，"+jsonObject.optString("data"), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else if (jsonObject.optString("state").equals("0") && jsonObject.optString("msg").equals("时间错误")) {
                            Snackbar snackbar = Snackbar.make(bt_set_end_time, jsonObject.optString("data"), Snackbar.LENGTH_LONG);
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
                Snackbar snackbar = Snackbar.make(bt_set_end_time, "设置打卡时间出错，请检查服务器运行环境", Snackbar.LENGTH_LONG);
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
