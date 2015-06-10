package cn.edu.pkusz.battery.fragment;


import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;

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
import cn.edu.pkusz.battery.common.Constants;
import cn.edu.pkusz.battery.common.GlobalApplication;
import cn.edu.pkusz.battery.power.BatteryStatus;
import cn.edu.pkusz.battery.power.ModeManager;
import cn.edu.pkusz.battery.power.StandbyTime;
import cn.edu.pkusz.battery.service.TimerService;

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
    private boolean visibleFlag = true;
    private Thread thread=null;

    private TextView mBatteryTemperature;
    private TextView mBatteryVoltage;
    private TextView mBatteryCraft;
    private TextView mAvailableTimeTextView;
    
    private int batteryLevelPercent = 0;
    private int batteryScale = 100;
    private int batteryLevel = 0;
    private int batteryTemperature = 0;
    private int batteryVoltage = 0;
    private String batteryHealth = "未知";
    private String batteryCraft = "Li-ion";
    private String batteryStatus = "未知";
    private ModeManager mmanager  = null;
    private StandbyTime standbyTime = null;
    
    private BatteryChangedReceiver mBatteryChangedReceiver = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getActionBar().setTitle(R.string.app_name);

        view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        mBatteryInfo = (LinearLayout)view.findViewById(R.id.battery_info);
        mBatteryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BatteryInfoActivity.class);
                intent.putExtra(BATTERY_HEALTH, batteryHealth);
                intent.putExtra(BATTERY_LEVEL, batteryLevelPercent);
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
        mAvailableTimeTextView = (TextView) view.findViewById(R.id.available_time);
        //注册一个接收器
        mBatteryChangedReceiver = new BatteryChangedReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TimerService.BATTERY_CHANGED_ACTION);
        getActivity().registerReceiver(mBatteryChangedReceiver, intentFilter);
        //获取初始化的电池电量
        this.batteryLevel = (int)(BatteryStatus.getBatteryLevel()*100);
        this.batteryLevelPercent = batteryLevel;
        this.batteryTemperature = BatteryStatus.getBatteryTemperature();
        this.batteryVoltage = BatteryStatus.getBatteryVoltage();
        
        mBatteryTemperature.setText(getFloatValue(batteryTemperature * 0.1) + " ℃");
        mBatteryVoltage.setText(getFloatValue(batteryVoltage / 1000.0) + " V");
        
        mmanager = new ModeManager(getActivity().getApplicationContext());
        standbyTime = new StandbyTime(getActivity().getApplicationContext(),mmanager);
        return view;
    }

    //标志位，表示是否是第一次启动
    private boolean stopped = false;
    @Override
    public void onResume() {
        super.onResume();
        visibleFlag = true;
        if(stopped==false)
        {
        	stopped=true;
        	setProgress(this.batteryLevelPercent,true);
        }
        else {
        	setProgress(this.batteryLevelPercent,false);
		}
    }

    @Override
    public void onPause() {
        super.onPause();
        visibleFlag = false;
    }

    @Override
    public void onDestroy() {
    	getActivity().unregisterReceiver(mBatteryChangedReceiver);
        super.onDestroy();
    }

    /*
        当fragment重新显示时选择重绘环形进度条
        */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (this.isVisible()) {
            getActivity().getActionBar().setTitle(R.string.app_name);
            visibleFlag = true;
            setProgress(batteryLevelPercent,true);
        }else {
            visibleFlag = false;
        }
    }

    public synchronized void setProgress(int batteryLevel,boolean forceRepaint) {
    	//如果电量没有变化就不重绘
    	if(forceRepaint == false  && batteryLevel == this.batteryLevelPercent) return;
        if (thread != null && thread.isAlive()) {
            return;
        }
        this.batteryLevelPercent = batteryLevel;
        final int level = this.batteryLevelPercent;
        donutProgress.setProgress(0);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int progress = 0;
                    Thread.sleep(300);
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
   
   public static float getFloatValue(double f)
   {
	   BigDecimal   b   =   new   BigDecimal(f);  
	   return b.setScale(1,   BigDecimal.ROUND_HALF_UP).floatValue();  
   }
   
  /**
   * 接收service的广播，更新电量进度条
   * @author 陶世博
   *
   */
   private class BatteryChangedReceiver extends BroadcastReceiver {
       private final String TAG = BatteryChangedReceiver.class.getSimpleName();
       @Override
       public void onReceive(Context context, Intent intent) {
    	   int batteryLevel_tmp = intent.getIntExtra(TimerService.BATTERY_LEVEL, 0);
    	   int batteryScale_tmp = intent.getIntExtra(TimerService.BATTERY_SCALE, 100);
    	   int batteryVoltage_tmp = intent.getIntExtra(TimerService.BATTERY_VOLTAGE, 0);
    	   int batteryTemperature_tmp = intent.getIntExtra(TimerService.BATTERY_TEMPERATURE, 0);
           String batteryCraft_tmp = intent.getStringExtra(TimerService.BATTERY_CRAFT);
           String batteryHealth_tmp = intent.getStringExtra(TimerService.BATTERY_HEALTH);
           String batteryStatus_tmp = intent.getStringExtra(TimerService.BATTERY_STATUS);
           
    	   if(batteryTemperature != batteryTemperature_tmp)
           {
        	   batteryTemperature = batteryTemperature_tmp;
        	   mBatteryTemperature.setText(getFloatValue(batteryTemperature * 0.1) + " ℃");
           }
           if(batteryVoltage != batteryVoltage_tmp)
           {
        	   batteryVoltage=batteryVoltage_tmp;
        	   mBatteryVoltage.setText(getFloatValue(batteryVoltage / 1000.0) + " V");
           }
           if(!batteryCraft.equals(batteryCraft_tmp))
           {
        	   batteryCraft=batteryCraft_tmp;
        	   mBatteryCraft.setText(batteryCraft);
           }
           batteryHealth = batteryHealth_tmp;
           batteryLevel = batteryLevel_tmp;
           batteryScale = batteryScale_tmp;
           mAvailableTimeTextView.setText(standbyTime.getStandbyTimeUsingHistory(batteryLevel));
           if(batteryStatus.equals(batteryStatus_tmp))
           {
        	   setProgress((int) ((batteryLevel / (float) batteryScale) * 100),false);
           }
           else
           {
        	   setProgress((int) ((batteryLevel / (float) batteryScale) * 100),true);
           }
           batteryStatus = batteryStatus_tmp;
       }
   }
}
