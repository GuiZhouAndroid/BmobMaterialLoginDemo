package arroon.android.login.bmobobject;

import android.os.AsyncTask;

import java.util.List;

import arroon.android.login.utils.CCallBack;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;

/**
 * 用户实体
 */

public class BmobUser extends BmobObject {

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    public BmobUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    final static int EXIST = -1;
    final static int ERROR = -2;
    final static int OK = -3;

    /**
     * 创建用户
     */
    public static void register(final String username, final String password, final CCallBack cCallBack) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {

                //判断是否有重复用户
                BmobQuery<BmobUser> bmobQuery = new BmobQuery<>();
                bmobQuery.addWhereEqualTo("username", username);
                List<BmobUser> bmobUserList = null;
                try {
                    bmobUserList = bmobQuery.findObjectsSync(BmobUser.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (bmobUserList != null && bmobUserList.size() > 0) {
                    return EXIST;
                }

                BmobUser bmobUser = new BmobUser(username, password);
                try {
                    bmobUser.saveSync();
                } catch (Exception e) {
                    e.printStackTrace();
                    return ERROR;
                }
                return OK;
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                switch (result) {
                    case OK:
                        cCallBack.onSuccess(OK);
                        break;
                    case EXIST:
                        cCallBack.onFailure(EXIST, "用户已存在");
                        break;
                    default:
                        cCallBack.onFailure(ERROR, "注册失败");
                        break;
                }
            }
        }.execute();
    }

    /**
     * 登录
     */
    public static void login(final String username, final String password, final CCallBack cCallBack) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {

                BmobQuery<BmobUser> bmobQuery = new BmobQuery<>();
                bmobQuery.addWhereEqualTo("username", username);
                bmobQuery.addWhereEqualTo("password", password);
                List<BmobUser> bmobUserList = null;
                try {
                    bmobUserList = bmobQuery.findObjectsSync(BmobUser.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (bmobUserList != null && bmobUserList.size() > 0) {
                    return OK;
                }
                return ERROR;
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                switch (result) {
                    case OK:
                        cCallBack.onSuccess(OK);
                        break;
                    case ERROR:
                        cCallBack.onFailure(EXIST, "用户名或密码错误");
                        break;
                    default:
                        break;
                }
            }
        }.execute();
    }

}
