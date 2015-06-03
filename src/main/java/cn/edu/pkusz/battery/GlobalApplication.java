package cn.edu.pkusz.battery;

import android.app.Application;
import android.content.Context;

/**
 * Created by 陶世博 on 2015/6/2.
 */
public class GlobalApplication extends Application{
    /**
     * 编写自己的Application，管理全局状态信息，比如Context
     *
     * @author yy
     */
    private static Context context;

    @Override
    public void onCreate() {
        //获取Context
        context = getApplicationContext();
    }

    //返回
    public static Context getContext() {
        return context;
    }

}
