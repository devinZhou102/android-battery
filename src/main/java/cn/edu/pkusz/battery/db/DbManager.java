package cn.edu.pkusz.battery.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.Map;

import cn.edu.pkusz.battery.GlobalApplication;
import cn.edu.pkusz.battery.network.TrafficInfo;

/**
 * Created by 陶世博 on 2015/6/3.
 */
public class DbManager {
    private DbHelper dbHelper;
    private SQLiteDatabase wDb;
    private SQLiteDatabase rDb;

    public DbManager(Context context) {
        dbHelper = new DbHelper(GlobalApplication.getContext());
        wDb = dbHelper.getWritableDatabase();
        rDb = dbHelper.getReadableDatabase();
    }

    public void close() {
        wDb.close();
        rDb.close();
        dbHelper.close();
    }

    //------------table battery mathods begin----------------
    public long battery_insert(BatteryLevelEntry entry) {
        return BatteryTable.insert(wDb, entry);
    }
    public List<BatteryLevelEntry> battery_query(Long start, Long end) {
        return BatteryTable.query(rDb, start, end);
    }
    public void battery_showAll() {
        BatteryTable.showAll(rDb);
    }
    public void battery_drop() {
        BatteryTable.drop(wDb);
    }
    //------------table battery mathods end----------------
    //------------table Traffic mathods begin----------------
    public void traffic_updateEnd(String pacakgeName, long endTime, long rx, long tx)
    {
        TrafficTable.updateEnd(rDb, wDb, pacakgeName, endTime, rx, tx);
    }
    public void traffic_insertStart(String pacakgeName, String appName, long startTime,String networkType, long rx, long tx)
    {
        TrafficTable.insertStart(wDb, pacakgeName, appName, startTime, networkType, rx, tx);
    }
    public Map<String, TrafficInfo> traffic_queryTotal() {
        return TrafficTable.queryTotal(rDb);
    }
}
