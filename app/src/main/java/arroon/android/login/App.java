package arroon.android.login;

import android.app.Application;

import cn.bmob.v3.Bmob;

public class App extends Application {

    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     * If you override this method, be sure to call super.onCreate().
     */
    @Override
    public void onCreate() {
        super.onCreate();

        //1.初始化，配置bmobAppkey
        Bmob.initialize(this, "1f6406597d7e42d492c21eacb8f103d7");
    }
}
