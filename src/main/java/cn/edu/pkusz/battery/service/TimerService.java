package cn.edu.pkusz.battery.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.GregorianCalendar;

import cn.edu.pkusz.battery.common.Constants;
import cn.edu.pkusz.battery.common.Static;
import cn.edu.pkusz.battery.db.BatteryDbHelper;
import cn.edu.pkusz.battery.db.BatteryLevelEntry;
import cn.edu.pkusz.battery.power.BatteryStatus;

/**
 * Created by 陶世博 on 2015/6/1.
 */
public class TimerService extends Service{

    private static BatteryDbHelper batteryDbHelper = null;
    private Handler handler = new Handler();

    static {
        batteryDbHelper = Static.getBatteryDbHelper();

    }

    private Runnable mTasks = new Runnable()
    {
        public void run() {
            // 添加具体需要做的事情：
            long now = new GregorianCalendar().getTimeInMillis();
            float level = BatteryStatus.getBatteryLevel();
            batteryDbHelper.insert(new BatteryLevelEntry(now, level));
            handler.postDelayed(mTasks, getInterval());
        }
    };

    private long getInterval() {
        return Constants.INTERVAL;
    }

    @Override
    public void onCreate() {
        Log.e("timestamp service:", "onCreated");
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
