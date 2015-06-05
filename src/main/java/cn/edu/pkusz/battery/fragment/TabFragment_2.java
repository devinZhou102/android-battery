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
import android.widget.TextView;
import cn.edu.pkusz.battery.R;
import cn.edu.pkusz.battery.circleprogress.CircleProgress;
import cn.edu.pkusz.battery.power.BatteryStatus;

/**
 * xin
 */
public class TabFragment_2 extends Fragment {

	private View rootView;
	private CircleProgress mCircleProgress = null;
	private TextView mChargingStatus = null;
	private BatteryReceiver tReceiver;
	private boolean visibleFlag = true;
	private int batteryLevel = 0;
	private Thread thread = null;


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.tab_fragment_2, container, false);
		mCircleProgress = (CircleProgress) rootView.findViewById(R.id.circle_progress);
		mChargingStatus = (TextView) rootView.findViewById(R.id.charging_status);

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

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		visibleFlag = true;
		setProgress(this.batteryLevel);
		updateChargingStatus(BatteryStatus.isCharging() ? (R.string.charging) : (R.string.nocharging));
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
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
			setProgress(this.batteryLevel);
		} else {
			visibleFlag = false;
		}
	}

	/*
	显示动态变化过程
	 */

	public synchronized void setProgress(int batteryLevel) {
		//防止两个线程同时执行，导致混乱
		if (thread != null && thread.isAlive()) {
			return;
		}
		this.batteryLevel = batteryLevel;
		final int level = this.batteryLevel;
		mCircleProgress.setProgress(0);
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					int progress = 0;
					Thread.sleep(500);
					while (progress <= level && visibleFlag == true) {
						final int finalProgress = progress;
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mCircleProgress.setProgress(finalProgress);
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

	public void updateChargingStatus(int status) {
		mChargingStatus.setText(getString(status));
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

			int batteryVoltage = intent.getIntExtra("voltage", 0);
			int batteryTemperature = intent.getIntExtra("temperature", 0);
			Log.e(TAG, "电量：" + batteryLevel + "\t" + batteryVoltage + "\t" + batteryTemperature);
			//记录电池状态
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
				switch (intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN)) {
					case BatteryManager.BATTERY_STATUS_CHARGING:
						updateChargingStatus(R.string.charging);
						break;
					case BatteryManager.BATTERY_STATUS_DISCHARGING:
						updateChargingStatus(R.string.nocharging);
						break;
					case BatteryManager.BATTERY_STATUS_FULL:
						updateChargingStatus(R.string.chargingFull);
						break;
					case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
						updateChargingStatus(R.string.nocharging);
						break;
					default:
						Log.e(TAG, "电池状态未知");
						break;
				}
			} else if (action.equals(Intent.ACTION_BATTERY_LOW)) {
				Log.e(TAG, "low power");
			}
		}
	}
}
