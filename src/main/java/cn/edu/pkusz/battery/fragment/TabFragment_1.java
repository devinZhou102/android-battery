package cn.edu.pkusz.battery.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.edu.pkusz.battery.R;
import cn.edu.pkusz.battery.activity.BatteryInfoActivity;
import cn.edu.pkusz.battery.circleprogress.DonutProgress;
import cn.edu.pkusz.battery.power.BatteryStatus;


/**
 * xin
 */
public class TabFragment_1 extends Fragment {
    private static final int LOW_POWER_LIMIT = 20;
    private View view;
    private DonutProgress donutProgress;
    private LinearLayout mBatteryInfo;
    private BatteryReceiver tReceiver;
    private boolean visibleFlag = true;
    private Thread thread=null;
    private int batteryLevel = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        mBatteryInfo = (LinearLayout)view.findViewById(R.id.battery_info);
        mBatteryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BatteryInfoActivity.class);
                startActivity(intent);
            }
        });
        donutProgress = (DonutProgress) view.findViewById(R.id.donut_progress);
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

    /**
    显示电量环形进度条, 如果电量低于 {@link #LOW_POWER_LIMIT}
     */
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
            int level = intent.getIntExtra("level", 0);
            int scale = intent.getIntExtra("scale", 100);
            int batteryLevel = (int) ((level / (float) scale) * 100);
            setProgress(batteryLevel);
        }
    }
}
