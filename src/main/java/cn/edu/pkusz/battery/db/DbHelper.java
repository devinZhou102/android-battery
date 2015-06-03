package cn.edu.pkusz.battery.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import cn.edu.pkusz.battery.common.Constants;

/**
 * Created by 陶世博 on 2015/6/3.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getSimpleName();

    public DbHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "create tables");
        createTables(db);
    }

    private void createTables(SQLiteDatabase db) {
        db.execSQL(BatteryTable.SQL_CREATE_TABLE);
        db.execSQL(TrafficTable.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "update database");
        dropTables(db);
        onCreate(db);
    }

    private void dropTables(SQLiteDatabase db) {
        db.execSQL(BatteryTable.SQL_DELETE_TABLE);
        db.execSQL(TrafficTable.SQL_DELETE_TABLE);
    }
}
