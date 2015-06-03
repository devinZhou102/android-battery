package cn.edu.pkusz.battery.common;

/**
 * Created by 陶世博 on 2015/6/2.
 */
public class Constants {
    public static final String DATABASE_NAME = "battery.db"; //数据库名称
    public static final int DATABASE_VERSION = 3; //递增的数据库版本,当数据库的模式发生变化时，只需要修改一下该值，即可自动调用DbHelper中的onUpgrade方法

    public static final int INTERVAL = 1000*60*10;
    public static final int BATTERY_LEVEL_CHART_POINT_NUM = 145;

    public static final String NETWORK_TYPE_MOBILE = "MOBILE";
    public static final String NETWORK_TYPE_WIFI = "WIFI";

}
