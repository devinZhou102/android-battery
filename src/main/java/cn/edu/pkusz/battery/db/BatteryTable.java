package cn.edu.pkusz.battery.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.pkusz.battery.common.Static;

/**
 * Created by 陶世博 on 2015/6/3.
 */
public class BatteryTable
{
    public static final String TABLE_NAME = "battery";
    public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    public static final String COLUMN_NAME_LEVEL = "level";
    public static final String COLUMN_ISCHARGING = "ischarging";
    
    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String REAL_TYPE = " REAL";
    public static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME_TIMESTAMP + TEXT_TYPE + COMMA_SEP
                    + COLUMN_NAME_LEVEL + REAL_TYPE + COMMA_SEP
                    + COLUMN_ISCHARGING + INTEGER_TYPE
                    + " )";
    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    /*
插入新数据
 */
    public static long insert(SQLiteDatabase db ,BatteryLevelEntry entry) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TIMESTAMP, entry.timestamp);
        values.put(COLUMN_NAME_LEVEL, entry.level);
        values.put(COLUMN_ISCHARGING, entry.isCharging);
        return db.insert(TABLE_NAME, "null", values);
    }

    /*
    查询制定日期之间的电量数据
     */
    public static List<BatteryLevelEntry> query(SQLiteDatabase db ,Long start, Long end) {

        String[] projection = {COLUMN_NAME_TIMESTAMP, COLUMN_NAME_LEVEL, COLUMN_ISCHARGING};
        String sortOrder = COLUMN_NAME_TIMESTAMP + " ASC";
        String selection = COLUMN_NAME_TIMESTAMP + " between ? and ?";

        Cursor cursor = db.query(TABLE_NAME, projection, selection, new String[]{start.toString(),end.toString()}, null, null, sortOrder);

        List<BatteryLevelEntry> result = new ArrayList<BatteryLevelEntry>();
        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                BatteryLevelEntry entry = new BatteryLevelEntry(cursor.getLong(0), cursor.getFloat(1),cursor.getInt(2)==0?false:true);
                result.add(entry);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return result;
    }

    /*
    输出所有记录
     */
    public static void showAll(SQLiteDatabase db) {
        Log.e("battery", "====================begin======================");

        String[] projection = {COLUMN_NAME_TIMESTAMP, COLUMN_NAME_LEVEL,COLUMN_ISCHARGING};
        String sortOrder = COLUMN_NAME_TIMESTAMP + " ASC";

        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, sortOrder);

        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Log.e("data", Static.getDateFormatLong().format(new Date(cursor.getLong(0))) + "   " + cursor.getFloat(1)+ "   " + cursor.getInt(2));
                cursor.moveToNext();
            }
        }
        cursor.close();
        Log.e("battery", "====================end======================");
    }

    public static void drop(SQLiteDatabase db) {
        db.delete(TABLE_NAME, null, null);
    }
}
