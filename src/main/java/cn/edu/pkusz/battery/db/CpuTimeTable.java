package cn.edu.pkusz.battery.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.pkusz.battery.common.Static;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class CpuTimeTable {
	public static final String TABLE_NAME = "cputime";
	public static final String COLUMN_NAME_TIME = "time";
	public static final String COLUMN_NAME_VALUE = "value";
	public static final String COLUMN_NAME_PID = "pid";
	
	public static final String TEXT_TYPE = " TEXT";
	public static final String INTEGER_TYPE = " INTEGER";
	public static final String REAL_TYPE = " REAL";
	public static final String COMMA_SEP = ",";

	//创建表
	public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
			+ " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+COLUMN_NAME_PID+INTEGER_TYPE+COMMA_SEP+
			 COLUMN_NAME_TIME + REAL_TYPE + COMMA_SEP + COLUMN_NAME_VALUE + REAL_TYPE + " )";
	//删除表
    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    //插入
    public static long insert(SQLiteDatabase db ,CpuTimeEntry entry) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_PID, entry.pid);
        values.put(COLUMN_NAME_TIME, entry.time);
        values.put(COLUMN_NAME_VALUE, entry.value);
        return db.insert(TABLE_NAME, "null", values);
    }
    //刪除数据库
    public static void drop(SQLiteDatabase db) {
        db.delete(TABLE_NAME, null, null);
    }
    
    public static List<CpuTimeEntry> query(SQLiteDatabase db, int pid) {
    	 String[] projection = {COLUMN_NAME_PID, COLUMN_NAME_TIME, COLUMN_NAME_VALUE};
         String sortOrder = COLUMN_NAME_PID + " ASC";
         String selection = COLUMN_NAME_PID + "==?";
    	Cursor cursor = db.query(TABLE_NAME, projection, selection, new String[]{String.valueOf(pid)}, null, null, sortOrder);
    	  List<CpuTimeEntry> result = new ArrayList<CpuTimeEntry>();
    	  int count = cursor.getCount();
    	  
          if (count > 0) {
              cursor.moveToFirst();
              while (!cursor.isAfterLast()) {
            	  CpuTimeEntry entry = new CpuTimeEntry(cursor.getInt(0),cursor.getLong(1), cursor.getLong(2));
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
        Log.e("cputime", "====================begin======================");

   	 String[] projection = {COLUMN_NAME_PID, COLUMN_NAME_TIME, COLUMN_NAME_VALUE};
        String sortOrder = COLUMN_NAME_PID + " ASC";

        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, sortOrder);

        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Log.e("cpudata", Static.getDateFormatLong().format(new Date(cursor.getInt(0))) + "   " + cursor.getLong(1)+ "   " + cursor.getLong(2));
                cursor.moveToNext();
            }
        }
        cursor.close();
        Log.e("cputimedone", "====================end======================");
    }
}
