package cn.edu.pkusz.battery.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import cn.edu.pkusz.battery.GlobalApplication;
import cn.edu.pkusz.battery.db.BatteryDbHelper;

/**
 * Created by 陶世博 on 2015/6/2.
 */
public class Static {
    private static BatteryDbHelper batteryDbHelper = null;
    private static GregorianCalendar calendar = null;
    private static SimpleDateFormat dateFormat = null;

    public static BatteryDbHelper getBatteryDbHelper() {
        if (batteryDbHelper == null) {
            batteryDbHelper = new BatteryDbHelper(GlobalApplication.getContext());
        }
        return batteryDbHelper;
    }

    public static Calendar getCalendar() {
        if (calendar == null) {
            calendar = new GregorianCalendar();
        }
        return calendar;
    }

    public static SimpleDateFormat getDateFormat() {
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        }
        return dateFormat;
    }
}
