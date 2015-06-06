package cn.edu.pkusz.battery.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.edu.pkusz.battery.R;
import cn.edu.pkusz.battery.activity.BatteryInfoActivity;
import cn.edu.pkusz.battery.circleprogress.DonutProgress;
import cn.edu.pkusz.battery.power.BatteryStatus;


/**
 * xin
 */
public class TabFragment_1 extends Fragment {
    public static final String BATTERY_TEMPERATURE = "battery_temperature";
    public static final String BATTERY_VOLTAGE = "battery_voltage";
    public static final String BATTERY_SCALE = "battery_scale";
    public static final String BATTERY_LEVEL = "battery_level";
    public static final String BATTERY_HEALTH = "battery_health";
    public static final String BATTERY_CRAFT = "battery_craft";


    private View view;
    private DonutProgress donutProgress;
    private LinearLayout mBatteryInfo;
    private BatteryReceiver tReceiver;
    private boolean visibleFlag = true;
    private Thread thread=null;

    private TextView mBatteryTemperature;
    private TextView mBatteryVoltage;
    private TextView mBatteryCraft;

    private int batteryScale = 100;
    private int batteryLevel = 0;
    private int batteryTemperature = 0;
    private int batteryVoltage = 0;
    private String batteryHealth = "未知";
    private String batteryCraft = "Li-ion";
    private String batteryStatus = "未知";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        mBatteryInfo = (LinearLayout)view.findViewById(R.id.battery_info);
        mBatteryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BatteryInfoActivity.class);
                intent.putExtra(BATTERY_HEALTH, batteryHealth);
                intent.putExtra(BATTERY_LEVEL, batteryLevel);
                intent.putExtra(BATTERY_SCALE, batteryScale);
                intent.putExtra(BATTERY_TEMPERATURE, batteryTemperature);
                intent.putExtra(BATTERY_VOLTAGE, batteryVoltage);
                intent.putExtra(BATTERY_CRAFT, "Li-ion");
                startActivity(intent);
            }
        });
        donutProgress = (DonutProgress) view.findViewById(R.id.donut_progress);
        mBatteryTemperature = (TextView) view.findViewById(R.id.battery_temperature);
        mBatteryVoltage = (TextView) view.findViewById(R.id.battery_voltage);
        mBatteryCraft = (TextView) view.findViewById(R.id.battery_craft);


        // 注册BatteryReceiver
        tReceiver = new BatteryReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        getActivity().registerReceiver(tReceiver, filter);

        //获取初始化的电池电量
        this.batteryLevel = (int) (BatteryStatus.getBatteryLevel() * 100);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        visibleFlag = true;
        setProgress(batteryLevel);
    }

    @Override
    public void onPause() {
        super.onPause();
        visibleFlag = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(tReceiver);
    }

    /*
        当fragment重新显示时选择重绘环形进度条
        */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (this.isVisible()) {
            visibleFlag = true;
            setProgress(batteryLevel);
        }else {
            visibleFlag = false;
        }
    }

    public synchronized void setProgress(int batteryLevel) {
        if (thread != null && thread.isAlive()) {
            return;
        }
        this.batteryLevel = batteryLevel;
        final int level = this.batteryLevel;

        donutProgress.setProgress(0);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int progress = 0;
                    Thread.sleep(500);
                    while (progress <= level && visibleFlag==true) {
                        final int finalProgress = progress;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                donutProgress.setProgress(finalProgress);
                            }
                        });
                        Thread.sleep(10);
                        progress += 1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    class BatteryReceiver extends BroadcastReceiver {
        private final String TAG = BatteryReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "充电状态改变");
            //获取当前电量
            batteryLevel = intent.getIntExtra("level", 0);
            batteryScale = intent.getIntExtra("scale", 100);
            batteryVoltage = intent.getIntExtra("voltage", 0);  //电池电压
            batteryTemperature = intent.getIntExtra("temperature", 0);  //电池温度

            switch (intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN))
            {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    batteryStatus = getString(R.string.charging);
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    batteryStatus = getString(R.string.discharging);
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    batteryStatus = getString(R.string.nocharging);
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    batteryStatus = getString(R.string.chargingFull);
                    break;
                default:
                    batteryStatus = "未知道状态";
                    break;
            }

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
            updateFragment();
        }
    }

    /*
    更新电池的电压、温度等信息, 重绘环形进度条
     */
    private void updateFragment() {
        mBatteryTemperature.setText(this.batteryTemperature * 0.1 + " ℃");
        mBatteryVoltage.setText(this.batteryVoltage / 1000.0 + " V");
        mBatteryCraft.setText(this.batteryCraft);

        setProgress((int) ((batteryLevel / (float) batteryScale) * 100));
    }
}
