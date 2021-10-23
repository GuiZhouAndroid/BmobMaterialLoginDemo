package com.yj.system.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yj.systemc.R;

/**
 * created by on 2021/10/23
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-10-23-22:25
 */
public class InSchoolActivity extends AppCompatActivity {
    private WebView wv_in_school;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_school);
        //WebView初始化获得实例
        wv_in_school= (WebView) findViewById(R.id.wv_in_school);
        //getSettings()方法是设置实例的属性，setJavaScriptEnabled()方法只是其中一种，
        // 在当前WebView中支持JavaScript脚本，结果为true
        wv_in_school.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        wv_in_school.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        wv_in_school.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        //在当前WebView中在跳转下一个网页的时，仍然是在当前WebView显示，否则就打开系统浏览器
        wv_in_school.setWebViewClient(new WebViewClient());
        //在当前WebView中打开的网址
        wv_in_school.loadUrl("http://192.168.157.29:8083/login");
        wv_in_school.getSettings().setUseWideViewPort(true);
        wv_in_school.getSettings().setLoadWithOverviewMode(true);
        WebSettings webSettings =wv_in_school.getSettings();
        webSettings.setSupportZoom(true);
    }

    /**
     * 网页中退回上一页
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 如果有历史记录，就检查关键事件是否有后退键
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv_in_school.canGoBack()) {
            // 返回键退回
            wv_in_school.goBack();
            return true;
        }
        // 如果它不是后退键或者没有网页历史，就会退出
        // 系统默认行为（可能退出活动）
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wv_in_school != null) {
            wv_in_school.stopLoading();
            wv_in_school.clearHistory();
            wv_in_school.clearCache(true);
            wv_in_school.loadUrl("about:blank");
            wv_in_school.pauseTimers();
            wv_in_school = null;
        }
    }
}
