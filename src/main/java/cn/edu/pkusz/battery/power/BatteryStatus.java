package cn.edu.pkusz.battery.power;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import cn.edu.pkusz.battery.common.GlobalApplication;

/**
 * Created by 陶世博 on 2015/5/30.
 */
public class BatteryStatus {
    public enum ChargingType{
        USB , AC
    }

    public static Intent getIntent() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
      return GlobalApplication.getContext().registerReceiver(null, intentFilter);
      //  return new Intent();
    }
    /*
    判断手机是否在充电
     */
    public static boolean isCharging() {
        int status = getIntent().getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
        return isCharging;
    }

    /*
    判断充电类型,此处充电类型并不完善，还有wireless等
     */
    public static ChargingType getChargingType() {
        int chargePlug = getIntent().getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        switch (chargePlug) {
            case BatteryManager.BATTERY_PLUGGED_USB:
                return ChargingType.USB;
            case BatteryManager.BATTERY_PLUGGED_AC:
                return ChargingType.AC;
            default:
                return null;
        }
    }

    public static float getBatteryLevel() {
        Log.e("battery", "get level");
        int level = getIntent().getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = getIntent().getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        return level / (float) scale;
    }
}
