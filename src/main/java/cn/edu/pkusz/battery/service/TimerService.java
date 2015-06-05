package cn.edu.pkusz.battery.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.GregorianCalendar;
import java.util.List;

import cn.edu.pkusz.battery.common.Constants;
import cn.edu.pkusz.battery.common.Static;
import cn.edu.pkusz.battery.db.BatteryLevelEntry;
import cn.edu.pkusz.battery.db.DbManager;
import cn.edu.pkusz.battery.network.TrafficStatus;
import cn.edu.pkusz.battery.power.BatteryStatus;

/**
 * Created by 陶世博 on 2015/6/1.
 */
public class TimerService extends Service{
    private static final String TAG = TimerService.class.getSimpleName();

    private DbManager dbManager = null;

    private ConnectivityManager connManager;
    private TrafficReceiver tReceiver;

    private Handler handler = new Handler();

    private Runnable mTasks = new Runnable()
    {
        public void run() {
            // 添加具体需要做的事情
            //采集电池电量信息
            collectBatteryLevel();
            
            //采集网络流量信息
            handler.postDelayed(mTasks, getInterval());
        }
    };

    private void collectTrafficAmount() {
        String networkType = TrafficStatus.getNetworkType(connManager);
        //只获取有网络访问权限的应用
        List<PackageInfo> packinfos = getPackageManager().getInstalledPackages(
                PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);
        for (PackageInfo info : packinfos) {
            String[] premissions = info.requestedPermissions;
            if (premissions != null && premissions.length > 0) {
                for (String premission : premissions) {
                    if ("android.permission.INTERNET".equals(premission)) {
                        int uid = info.applicationInfo.uid;
                        long rx = TrafficStats.getUidRxBytes(uid);
                        long tx = TrafficStats.getUidTxBytes(uid);
                        dbManager.traffic_updateEnd(info.packageName, System.currentTimeMillis(), rx, tx);
                        dbManager.traffic_insertStart(
                                info.packageName,
                                info.applicationInfo.loadLabel(getPackageManager()).toString(),
                                System.currentTimeMillis(), networkType, rx, tx);
                    }
                }
            }
        }
    }

    /**
     * 采集电池电量信息
     */
    private void collectBatteryLevel() {
        long now = new GregorianCalendar().getTimeInMillis();
        float level = BatteryStatus.getBatteryLevel();
        dbManager.battery_insert(new BatteryLevelEntry(now, level));
    }

    private long getInterval() {
        return Constants.INTERVAL;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreated");
        // 获得数据库连接服务
        dbManager = Static.getDbManager();
        // 获得网络连接服务
        connManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 注册TrafficReceiver
        tReceiver = new TrafficReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(tReceiver, filter);
        handler.postDelayed(mTasks, 1000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        unregisterReceiver(tReceiver);
//        collectTrafficAmount();
        dbManager.close();
        handler.removeCallbacks(mTasks);
        super.onDestroy();
    }

    private class TrafficReceiver extends BroadcastReceiver {
        private final String TAG = TrafficReceiver.class.getSimpleName();
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "网络状态改变");
//            collectTrafficAmount();
        }
    }
}
