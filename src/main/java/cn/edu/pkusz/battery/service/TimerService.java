package cn.edu.pkusz.battery.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Date;
import java.util.Timer;

/**
 * Created by 陶世博 on 2015/6/1.
 */
public class TimerService extends Service{

    Timer timer = null;
    private Handler handler = new Handler();
    //定时间隔
    public static final int INTERVAL = 1000;

    private Runnable mTasks = new Runnable()
    {

        public void run()
        {
            Log.e("task : ", "run "+ new Date());
            // 添加具体需要做的事情：

            handler.postDelayed(mTasks, INTERVAL);
        }
    };

    @Override
    public void onCreate() {
        Log.e("time service:", "onCreated");
        super.onCreate();
        handler.postDelayed(mTasks, 1000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("Time Service", "onDestroy");
        handler.removeCallbacks(mTasks);
    }
}
