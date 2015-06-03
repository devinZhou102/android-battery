package cn.edu.pkusz.battery.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.edu.pkusz.battery.R;
import cn.edu.pkusz.battery.circleprogress.DonutProgress;
import cn.edu.pkusz.battery.BatteryInfoActivity;
import cn.edu.pkusz.battery.power.BatteryStatus;


/**
 * xin
 */
public class TabFragment_1 extends Fragment {
    private static final int LOW_POWER_LIMIT = 20;
    private View view;
    private DonutProgress donutProgress;
    private LinearLayout mBatteryInfo;
    private boolean visibleFlag = true;

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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        visibleFlag = true;
        showLevelCircleProgress();
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
            showLevelCircleProgress();
        }else {
            visibleFlag = false;
        }
    }

    /**
    显示电量环形进度条, 如果电量低于 {@link #LOW_POWER_LIMIT}
     */
    public void  showLevelCircleProgress() {
        if (donutProgress == null) {
            donutProgress = (DonutProgress) getActivity().findViewById(R.id.donut_progress);
        }
        final int level = (int) (BatteryStatus.getBatteryLevel() * 100);
        if (level <= LOW_POWER_LIMIT) {
            donutProgress.setProgress(level);
            return;
        }
        donutProgress.setProgress(0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int progress = 0;
                    Thread.sleep(500);
                    while (progress < level && visibleFlag==true) {
                        progress += 1;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                donutProgress.setProgress(donutProgress.getProgress() + 1);
                            }
                        });
                        Thread.sleep(10);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }
}
