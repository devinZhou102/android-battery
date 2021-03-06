package cn.edu.pkusz.battery.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import cn.edu.pkusz.battery.db.DbManager;

/**
 * Created by 陶世博 on 2015/6/2.
 */
public class Static {
    private static DbManager dbManager = null;
    private static GregorianCalendar calendar = null;
    private static SimpleDateFormat dateFormatLong = null;
    private static SimpleDateFormat dateFormatShort = null;

    public static Calendar getCalendar() {
        if (calendar == null) {
            calendar = new GregorianCalendar();
        }
        return calendar;
    }

    public static SimpleDateFormat getDateFormatLong() {
        if (dateFormatLong == null) {
            dateFormatLong = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        }
        return dateFormatLong;
    }
    public static SimpleDateFormat getDateFormatShort() {
        if (dateFormatShort == null) {
            dateFormatShort = new SimpleDateFormat("yyyy-MM-dd");
        }
        return dateFormatShort;
    }

    public static DbManager getDbManager() {
        if (dbManager == null) {
            dbManager = new DbManager(GlobalApplication.getContext());
        }
        return dbManager;
    }
}
