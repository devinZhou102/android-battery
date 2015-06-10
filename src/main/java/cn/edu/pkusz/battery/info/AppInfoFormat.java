package cn.edu.pkusz.battery.info;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import cn.edu.pkusz.battery.info.ApplicationInfo.DrainType;

public class AppInfoFormat implements Comparable<AppInfoFormat> {

	private final Context mContext;
	private String name;
	private Drawable icon;
	private double value;
	long tcpBytesReceived;
	long tcpBytesSent;
	int uid;
	
	private double percent;
	double noCoveragePercent;
	private DrainType drainType;
	public int issystemapp;
	
	public String packageName;
	
	
	
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public long getTcpBytesReceived() {
		return tcpBytesReceived;
	}

	public void setTcpBytesReceived(long tcpBytesReceived) {
		this.tcpBytesReceived = tcpBytesReceived;
	}

	public long getTcpBytesSent() {
		return tcpBytesSent;
	}

	public void setTcpBytesSent(long tcpBytesSent) {
		this.tcpBytesSent = tcpBytesSent;
	}

	public int getIssystemapp() {
		return issystemapp;
	}

	public void setIssystemapp(int issystemapp) {
		this.issystemapp = issystemapp;
	}

	public AppInfoFormat(Context context, String pkgName, double time) {
		mContext = context;
		value = time;
		drainType = DrainType.APP;
		packageName = pkgName;
		getQuickNameIcon(pkgName);
	}

	public DrainType getDrainType() {
		return drainType;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public double getPercentOfTotal() {
		return percent;
	}

	@Override
	public int compareTo(AppInfoFormat other) {
		return (int) (other.getValue() - getValue());
	}

	private void getQuickNameIcon(String pkgName) {
		PackageManager pm = mContext.getPackageManager();
		try {
			ApplicationInfo appInfo = pm.getApplicationInfo(pkgName, 0);
			if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)																	
				issystemapp = 0;
			else
				issystemapp = 1;
			icon = appInfo.loadIcon(pm);
			name = appInfo.loadLabel(pm).toString();
			uid = appInfo.uid;
			tcpBytesReceived = TrafficStats.getUidRxBytes(uid);
			tcpBytesSent = TrafficStats.getUidTxBytes(uid);

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

}