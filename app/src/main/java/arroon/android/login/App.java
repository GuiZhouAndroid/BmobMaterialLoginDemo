package arroon.android.login;

import android.app.Application;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;

public class App extends Application {
    private static App myApp;

    public static ClearableCookieJar cookie;


    public static App getInstance() {
        return myApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
