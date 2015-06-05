package cn.edu.pkusz.battery.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;

import cn.edu.pkusz.battery.common.Constants;
import cn.edu.pkusz.battery.common.GlobalApplication;

/**
 * Created by 陶世博 on 2015/6/3.
 */
public class TrafficStatus {

    /*
    判断WIFI是否打开
     */
    public static boolean isWifiOn() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) GlobalApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWiFiNetworkInfo != null) {
            return mWiFiNetworkInfo.isAvailable();
        }
        return false;
    }
    public static boolean isWifiOn(ConnectivityManager connManager) {
        NetworkInfo mWiFiNetworkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWiFiNetworkInfo != null) {
            return mWiFiNetworkInfo.isAvailable();
        }
        return false;
    }
    /*
    判断数据流量是否可用
     */
    public static boolean isMobileOn() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) GlobalApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mMobileNetworkInfo != null) {
            return mMobileNetworkInfo.isAvailable();
        }
        return false;
    }
    public static boolean isMobileOn(ConnectivityManager connManager) {
        NetworkInfo mMobileNetworkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mMobileNetworkInfo != null) {
            return mMobileNetworkInfo.isAvailable();
        }
        return false;
    }

    /*
    获取当前网络连接类型
     */
    public static String getNetworkType(ConnectivityManager connManager) {
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return null;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return Constants.NETWORK_TYPE_WIFI;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return Constants.NETWORK_TYPE_MOBILE;
        }
        return null;
    }

    /*
    根据uid获取上传流量
     */
    public static long getTrafficUploadAmount(int uid) {
        try {
            return TrafficStats.getUidTxBytes(uid);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    /*
    根据uid获取下载流量
     */
    public static long getTrafficDownloadAmount(int uid) {
        try {
            return TrafficStats.getUidRxBytes(uid);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
