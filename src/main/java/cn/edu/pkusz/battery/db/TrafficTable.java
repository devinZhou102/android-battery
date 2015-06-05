package cn.edu.pkusz.battery.db;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import cn.edu.pkusz.battery.common.Constants;
import cn.edu.pkusz.battery.network.TrafficInfo;

/**
 * Created by 陶世博 on 2015/6/3.
 */
public class TrafficTable {
    public static final String TABLE_NAME = "traffic";

    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_PACKAGE_NAME = "package_name";
    public static final String COLUMN_APP_NAME = "app_name";
    public static final String COLUMN_START_TIME = "start_time";
    public static final String COLUMN_END_TIME = "end_time";
    public static final String COLUMN_NETWORK_TYPE = "network_type";
    public static final String COLUMN_RX = "rx";
    public static final String COLUMN_TX = "tx";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String NOT_NULL = " NOT NULL ";

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP
                    + COLUMN_PACKAGE_NAME + TEXT_TYPE + COMMA_SEP
                    + COLUMN_APP_NAME + TEXT_TYPE +COMMA_SEP
                    + COLUMN_START_TIME + INTEGER_TYPE + COMMA_SEP
                    + COLUMN_END_TIME + INTEGER_TYPE + COMMA_SEP
                    + COLUMN_NETWORK_TYPE + TEXT_TYPE + COMMA_SEP
                    + COLUMN_RX + INTEGER_TYPE + COMMA_SEP
                    + COLUMN_TX + INTEGER_TYPE
                    + " )";
    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static void updateEnd(SQLiteDatabase rdb,SQLiteDatabase wdb,String pacakgeName, long endTime, long rx, long tx) {
        // 查询最新的一条记录
        Cursor start = rdb.query(TABLE_NAME, null,
                COLUMN_PACKAGE_NAME + " = '" + pacakgeName
                        + "' and " + COLUMN_END_TIME + " is null",
                null, null, null, COLUMN_START_TIME + " desc", "1");
        if (!start.moveToFirst()) {
            return;
        }
        ContentValues value = new ContentValues();
        value.put(COLUMN_END_TIME, endTime);
        value.put(COLUMN_RX,rx - start.getLong(start.getColumnIndexOrThrow(COLUMN_RX)));
        value.put(COLUMN_TX,tx - start.getLong(start .getColumnIndexOrThrow(COLUMN_TX)));
        wdb.update(TABLE_NAME, value, COLUMN_ID + "=" + start.getInt(start.getColumnIndexOrThrow(COLUMN_ID)), null);
        start.close();
    }

    public static void insertStart(SQLiteDatabase wdb,String pacakgeName, String appName, long startTime,String networkType, long rx, long tx) {
        ContentValues value = new ContentValues();
        value.put(COLUMN_PACKAGE_NAME, pacakgeName);
        value.put(COLUMN_APP_NAME, appName);
        value.put(COLUMN_START_TIME, startTime);
        value.put(COLUMN_NETWORK_TYPE, networkType);
        value.put(COLUMN_RX, rx);
        value.put(COLUMN_TX, tx);
        wdb.insert(TABLE_NAME, null, value);
    }

    public static Map<String, TrafficInfo> queryTotal(SQLiteDatabase rdb) {
        Cursor c = rdb.rawQuery("select " + COLUMN_PACKAGE_NAME
                + "," + COLUMN_APP_NAME + ","
                + COLUMN_NETWORK_TYPE + ",sum("
                + COLUMN_RX + "),sum(" + COLUMN_TX
                + ") from " + TABLE_NAME + " where "
                + COLUMN_END_TIME + " is not null group by "
                + COLUMN_PACKAGE_NAME + ","
                + COLUMN_APP_NAME + ","
                + COLUMN_NETWORK_TYPE, null);
        Map<String, TrafficInfo> map = new HashMap<String, TrafficInfo>();
        while (c.moveToNext()) {
            String packageName = c.getString(0);
            TrafficInfo item = null;
            if (!map.containsKey(packageName)) {
                item = new TrafficInfo();
                item.packageName = packageName;
                item.appName = c.getString(1);
                map.put(packageName, item);
            } else {
                item = map.get(packageName);
            }
            String networkType = c.getString(2);
            if (networkType.equals(Constants.NETWORK_TYPE_MOBILE)) {
                item.mobileRx = c.getLong(3);
                item.mobileTx = c.getLong(4);
            } else if (networkType.equals(Constants.NETWORK_TYPE_WIFI)) {
                item.wifiRx = c.getLong(3);
                item.wifiTx = c.getLong(4);
            }
        }
        return map;
    }
}
