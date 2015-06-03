package cn.edu.pkusz.battery.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.pkusz.battery.GlobalApplication;
import cn.edu.pkusz.battery.common.Constants;
import cn.edu.pkusz.battery.common.Static;

/**
 * Created by 陶世博 on 2015/6/2.
 */
public class BatteryDbHelper extends SQLiteOpenHelper{
    private static final String TAG = "BatteryDbHelper";

    private static final String TABLE_NAME = "battery";
    private static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    private static final String COLUMN_NAME_LEVEL = "level";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + BaseColumns._ID + " INTEGER PRIMARY KEY,"
                    + COLUMN_NAME_TIMESTAMP + TEXT_TYPE + COMMA_SEP
                    + COLUMN_NAME_LEVEL + REAL_TYPE
                    + " )";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public BatteryDbHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /*
    插入新数据
     */
    public long insert(BatteryLevelEntry entry) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TIMESTAMP, entry.timestamp);
        values.put(COLUMN_NAME_LEVEL, entry.level);

        return db.insert(TABLE_NAME, "null", values);
    }

    /*
    查询制定日期之间的电量数据
     */
    public List<BatteryLevelEntry> query(Long start, Long end) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {COLUMN_NAME_TIMESTAMP, COLUMN_NAME_LEVEL};
        String sortOrder = COLUMN_NAME_TIMESTAMP + " ASC";
        String selection = COLUMN_NAME_TIMESTAMP + " between ? and ?";

        Cursor cursor = db.query(TABLE_NAME, projection, selection, new String[]{start.toString(),end.toString()}, null, null, sortOrder);

        List<BatteryLevelEntry> result = new ArrayList<BatteryLevelEntry>();
        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                BatteryLevelEntry entry = new BatteryLevelEntry(cursor.getLong(0), cursor.getFloat(1));
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
    public void selectAll() {
        Log.e("battery", "====================begin======================");
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {COLUMN_NAME_TIMESTAMP, COLUMN_NAME_LEVEL};
        String sortOrder = COLUMN_NAME_TIMESTAMP + " ASC";

        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, sortOrder);

        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Log.e("data", Static.getDateFormat().format(new Date(cursor.getLong(0))) + "   " + cursor.getFloat(1));
                cursor.moveToNext();
            }
        }
        cursor.close();
        Log.e("battery", "====================end======================");
    }

    public void deleteAll() {
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_NAME, null, null);
    }
}
