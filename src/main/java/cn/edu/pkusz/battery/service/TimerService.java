package cn.edu.pkusz.battery.service;

import java.util.GregorianCalendar;
import java.util.List;

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
import android.os.BatteryManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import cn.edu.pkusz.battery.R;
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
    public static final String BATTERY_CHANGED_ACTION="cn.edu.pkusz.action.BATTERY_CHANGED_ACTION";
    
    public static final String BATTERY_LEVEL="batteryLevel";
    public static final String BATTERY_SCALE="batteryScale";
    public static final String BATTERY_VOLTAGE="batteryVoltage";
    public static final String BATTERY_TEMPERATURE="batteryTemperature";
    public static final String BATTERY_STATUS= "batteryStatus";
    public static final String BATTERY_HEALTH = "batteryHealth";
    public static final String BATTERY_CRAFT = "batteryCraft";
    
    private DbManager dbManager = null;

    private ConnectivityManager connManager;
    private TrafficReceiver trafficReceiver;
    private BatteryReceiver batteryReceiver;
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

    private boolean isCharging = false;
    /**
     * 采集电池电量信息
     */
    private void collectBatteryLevel() {
        long now = new GregorianCalendar().getTimeInMillis();
        float level = BatteryStatus.getBatteryLevel();
        dbManager.battery_insert(new BatteryLevelEntry(now, level,isCharging));
    }
    private int batteryLevel_for_interval = 0;
    /**
     * 根据当前电量调整执行周期
     * @return
     */
    private long getInterval() {
    	int divisor = (int) Math.ceil(batteryLevel_for_interval / 10);
//        return Constants.INTERVAL + (10-divisor)*Constants.INTERVAL;
        return Constants.INTERVAL;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 获得数据库连接服务
        dbManager = Static.getDbManager();
        // 获得网络连接服务
        connManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 注册TrafficReceiver
        trafficReceiver = new TrafficReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(trafficReceiver, filter);
     // 注册BatteryReceiver
        batteryReceiver = new BatteryReceiver();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter1.addAction(Intent.ACTION_BATTERY_LOW);
        filter1.addAction(Intent.ACTION_POWER_CONNECTED);
        filter1.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(batteryReceiver, filter1);
        
        handler.postDelayed(mTasks, 1000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(trafficReceiver);
        unregisterReceiver(batteryReceiver);
//        collectTrafficAmount();
        dbManager.close();
        handler.removeCallbacks(mTasks);
        super.onDestroy();
    }

    /**
     * 监控网络变化的Receiver
     * @author 陶世博
     *
     */
    private class TrafficReceiver extends BroadcastReceiver {
        private final String TAG = TrafficReceiver.class.getSimpleName();
        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.e(TAG, "网络状态改变");
//            collectTrafficAmount();
        }
    }
    /**
     * 监控电池信息的Receiver
     * @author 陶世博
     *
     */
    private class BatteryReceiver extends BroadcastReceiver {
        private final String TAG = BatteryReceiver.class.getSimpleName();
        @Override
        public void onReceive(Context context, Intent intent) {
            
        	String batteryStatus="";
            switch (intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN))
            {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                	isCharging = true;
                	batteryStatus = getString(R.string.charging);
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                	isCharging = false;
                	batteryStatus = getString(R.string.discharging);
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                	isCharging = false;
                	batteryStatus = getString(R.string.nocharging);
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                	isCharging = true;
                	batteryStatus = getString(R.string.chargingFull);
                    break;
                default:
                	batteryStatus = "未知状态";
                	isCharging = false;
                    break;
            }
            String batteryHealth = "";
            switch (intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN))
            {
                case BatteryManager.BATTERY_HEALTH_GOOD:
                	batteryHealth = getString(R.string.health_good);
                    break;
                case BatteryManager.BATTERY_HEALTH_DEAD:
                	batteryHealth = getString(R.string.health_dead);
                    break;
                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                	batteryHealth = getString(R.string.health_over_voltage);
                    break;
                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                	batteryHealth =  getString(R.string.health_overheat);
                    break;
                default:
                	batteryHealth = getString(R.string.health_unknown);
                    break;
            }
          //获取当前电量
            int batteryLevel = intent.getIntExtra("level", 0);
            int batteryScale = intent.getIntExtra("scale", 100);
            int batteryVoltage = intent.getIntExtra("voltage", 0);  //电池电压
            int batteryTemperature = intent.getIntExtra("temperature", 0);  //电池温度
            batteryLevel_for_interval = batteryLevel;
            
            /*
             *广播电量变化消息 
             */
            Intent batteryChangedIntent = new Intent(TimerService.BATTERY_CHANGED_ACTION);
            batteryChangedIntent.putExtra(BATTERY_LEVEL, batteryLevel);
            batteryChangedIntent.putExtra(BATTERY_SCALE, batteryScale);
            batteryChangedIntent.putExtra(BATTERY_VOLTAGE, batteryVoltage);
            batteryChangedIntent.putExtra(BATTERY_TEMPERATURE, batteryTemperature);
            batteryChangedIntent.putExtra(BATTERY_CRAFT, "Li-ion");
            batteryChangedIntent.putExtra(BATTERY_HEALTH, batteryHealth);
            batteryChangedIntent.putExtra(BATTERY_STATUS, batteryStatus);
            sendBroadcast(batteryChangedIntent);
        }
    }
}
