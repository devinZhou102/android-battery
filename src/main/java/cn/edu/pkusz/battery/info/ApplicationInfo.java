package cn.edu.pkusz.battery.info;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

public class ApplicationInfo {
	public static long upData, downData;
	public static String[] pkgNames;
	public static double time = 9;

	private double mMinPercentOfTotal = 0;
	private long mStatsPeriod = 0;
	public double mMaxPower = 1;
	private double mTotalPower;

	private Context mContext;
	public int testType;

	public enum DrainType {
		IDLE, CELL, PHONE, WIFI, BLUETOOTH, SCREEN, APP, KERNEL, MEDIASERVER;
	}

	public ApplicationInfo(Context context) {
		testType = 1;
		mContext = context;
	}

	/**
	 * 设置最小百分比，小于该值的程序将被过滤掉
	 * 
	 * @param minPercentOfTotal
	 */
	public void setMinPercentOfTotal(double minPercentOfTotal) {
		this.mMinPercentOfTotal = minPercentOfTotal;
	}

	/**
	 * 获取消耗的总量
	 * 
	 * @return
	 */
	public double getTotalPower() {
		return mTotalPower;
	}

	/**
	 * 获取电池的使用时间
	 * 
	 * @return
	 */
	public String getStatsPeriod() {
		return Utils.formatElapsedTime(mContext, mStatsPeriod);
	}

	public List<AppInfoFormat> getBatteryStats() {

		return getAppListCpuTime();

	}

	private long getAppProcessTime(int pid) {
		FileInputStream flieInput = null;
		String ret = null;
		try {
			flieInput = new FileInputStream("/proc/" + pid + "/stat");
			byte[] buffer = new byte[1024];
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			int len = 0;
			while ((len = flieInput.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			ret = os.toString();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (flieInput != null) {
				try {
					flieInput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (ret == null) {
			return 0;
		}

		String[] s = ret.split(" ");
		if (s == null || s.length < 17) {
			return 0;
		}

		final long utime = string2Long(s[13]);
		final long stime = string2Long(s[14]);
		final long cutime = string2Long(s[15]);
		final long cstime = string2Long(s[16]);

		return utime + stime + cutime + cstime;
	}

	private long string2Long(String s) {
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
		}
		return 0;
	}

	private List<AppInfoFormat> getAppListCpuTime() {
		testType = 2;
		final List<AppInfoFormat> list = new ArrayList<AppInfoFormat>();

		long totalTime = 0;
		ActivityManager am = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();

		HashMap<String, AppInfoFormat> templist = new HashMap<String, AppInfoFormat>();
		for (RunningAppProcessInfo info : runningApps) {

			final long time = getAppProcessTime(info.pid);
			pkgNames = info.pkgList;
			
			if (pkgNames == null) {
				if (templist.containsKey(info.processName)) {
					AppInfoFormat sipper = templist.get(info.processName);
					sipper.setValue(sipper.getValue() + time);
				} else {
					templist.put(info.processName, new AppInfoFormat(mContext,
							info.processName, time));
				}
				totalTime += time;
			} else {
				for (String pkgName : pkgNames) {
					if (templist.containsKey(pkgName)) {
						AppInfoFormat sipper = templist.get(pkgName);
						sipper.setValue(sipper.getValue() + time);
					} else {
						templist.put(pkgName, new AppInfoFormat(mContext,
								pkgName, time));
					}
					totalTime += time;
				}
			}
		}

		if (totalTime == 0)
			totalTime = 1;
		time = totalTime;
		System.out.println("totalTime is " + time + " ..." + totalTime);
		list.addAll(templist.values());
		for (int i = list.size() - 1; i >= 0; i--) {
			AppInfoFormat sipper = list.get(i);
			double percentOfTotal = sipper.getValue() * 100 / totalTime;
			if (percentOfTotal < mMinPercentOfTotal) {
				list.remove(i);
			} else {
				sipper.setPercent(percentOfTotal);
			}
		}

		Collections.sort(list, new Comparator<AppInfoFormat>() {
			@Override
			public int compare(AppInfoFormat object1, AppInfoFormat object2) {
				double d1 = object1.getPercentOfTotal();
				double d2 = object2.getPercentOfTotal();
				if (d1 - d2 < 0) {
					return 1;
				} else if (d1 - d2 > 0) {
					return -1;
				} else {
					return 0;
				}
			}
		});

		return list;
	}

}
