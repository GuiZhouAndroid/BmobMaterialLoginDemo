package arroon.android.login.utils;

/**
 * Created by act on 2017/5/22.
 */
public abstract class CCallBack<T> {

    public abstract void onSuccess(T t);

    public abstract void onFailure(int code, String msg);
}
