package cn.edu.pkusz.battery.power;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import cn.edu.pkusz.battery.common.Constants;
import cn.edu.pkusz.battery.common.Static;
import cn.edu.pkusz.battery.db.BatteryLevelEntry;
import cn.edu.pkusz.battery.db.DbManager;

import android.content.Context;
import android.util.Log;

public class StandbyTime {
    double screenOnCurrent;
    double screenFullCurrent;
    double wifiOnCurrent ;
    double wifiActiveCurrent ;
    double audioCurrent ;
    double videoCurrent ;
    double radioOnCurrent ;
    double radioActiveCurrent ;
    double cpuAwakeCurrent ;
    double cpuIdleCurrent ;
    double cpuActiveCurrent;
    double bluetoothCurrent;
    double leftBattery;
    double batteryCapacity;
    int cpuSteps;
    private ModeManager mmanager ; 
    
    public StandbyTime(Context context , ModeManager modeManager){
    	mmanager = modeManager;
        try {
            Class clazz = null;
            Constructor con = null;
            clazz = Class.forName("com.android.internal.os.PowerProfile");
            con = clazz.getConstructor(Context.class);
            Object instance = con.newInstance(context);
            Method methodAVG = clazz.getDeclaredMethod("getAveragePower", String.class);
            Method methodNUM = clazz.getDeclaredMethod("getNumSpeedSteps");
            this.cpuSteps = (Integer)methodNUM.invoke(instance);
            this.batteryCapacity = (Double) methodAVG.invoke(instance, "battery.capacity");
            this.screenOnCurrent = (Double) methodAVG.invoke(instance, "screen.on");
            this.screenFullCurrent = (Double) methodAVG.invoke(instance, "screen.full");
            this.wifiOnCurrent = (Double) methodAVG.invoke(instance, "wifi.on");
            this.wifiActiveCurrent = (Double) methodAVG.invoke(instance, "wifi.active");
            this.audioCurrent = (Double) methodAVG.invoke(instance, "dsp.audio");
            this.videoCurrent = (Double) methodAVG.invoke(instance, "video.audio");
            this.radioOnCurrent = (Double) methodAVG.invoke(instance, "radio.on");
            this.radioActiveCurrent = (Double) methodAVG.invoke(instance, "radio.active");
            this.cpuAwakeCurrent = (Double) methodAVG.invoke(instance, "cpu.awake");
            this.cpuIdleCurrent = (Double) methodAVG.invoke(instance, "cpu.idle");
            this.cpuActiveCurrent = (Double) methodAVG.invoke(instance, "cpu.active");
            this.bluetoothCurrent = (Double) methodAVG.invoke(instance, "bluetooth.on");
            Log.e("cpusteps", this.cpuSteps + ", " + this.cpuAwakeCurrent);
        } catch (Exception ex) {
        	ex.printStackTrace();
            Log.e("test", ex.toString());
        }
    }
    public String getStandByTime(int batteryLevel){
    	this.leftBattery = batteryLevel * this.batteryCapacity / 100;
        int lifeInMinute = 0;
        try {
            double totalCurrent = this.cpuActiveCurrent* 100;
            if(mmanager.isBluetoothEnabled())
                totalCurrent += this.bluetoothCurrent;
            if(mmanager.isDataEnabled())
                totalCurrent += this.radioOnCurrent;
            if(mmanager.isWifiEnabled())
                totalCurrent += this.wifiOnCurrent;
            if(mmanager.isBluetoothEnabled())
            	totalCurrent+=this.bluetoothCurrent;
            totalCurrent += this.videoCurrent;
            totalCurrent += this.audioCurrent;
            
            lifeInMinute = (int)(this.leftBattery / totalCurrent * 60);
        }catch (Exception e){
            e.printStackTrace();
        }
        String result = "";
        result = lifeInMinute / 60 +  " h " + lifeInMinute % 60 + "m";
        return result;
    }
    /**
     *根据历史信息计算可用时间
     * @return
     */
    public String getStandbyTimeUsingHistory(int batteryLevel)
    {
    	String result = "";
    	DbManager dbManager = Static.getDbManager();
    	GregorianCalendar calendar = new GregorianCalendar();
    	long end = calendar.getTimeInMillis();
        calendar.add(Calendar.HOUR, -24);
        long start = calendar.getTimeInMillis();
        List<BatteryLevelEntry> list = dbManager.battery_query(start, end);
        int levelSum = 0 ;
        int timeSum = 0;
        for (int i = 1 ; i<list.size(); i++) {
        	BatteryLevelEntry entry2 = list.get(i);
        	BatteryLevelEntry entry1 = list.get(i-1);
        	if(entry1.isCharging==false && entry2.isCharging == false)
        	{
        		if(entry1.level >= entry2.level)
        		{
        			levelSum += entry1.level-entry2.level;
        			timeSum += entry2.timestamp - entry1.timestamp;
        		}
        	}
        }
        if(timeSum != 0)
        {
        	double rate = levelSum / timeSum;//每毫秒的耗电量
        	int remainTime = (int) (batteryLevel/rate)/(1000*60);//总的可用分钟数
        	result = remainTime / 60 +  " h " + remainTime % 60 + "m";
        }else {
        	result=getStandByTime(batteryLevel);
		}
    	return result;
    }
    public String getWifiTime(){
        int lifeInMinute = 0;
        try {
            double totalCurrent = this.cpuAwakeCurrent;
            if(mmanager.isBluetoothEnabled())
                totalCurrent += this.bluetoothCurrent;
            if(mmanager.isDataEnabled())
                totalCurrent += this.radioOnCurrent;
            if(mmanager.isWifiEnabled())
                totalCurrent += this.wifiActiveCurrent;
            totalCurrent += this.screenOnCurrent * mmanager.getBrightness() / 100;
            lifeInMinute = (int)(this.leftBattery / totalCurrent * 60);
        }catch (Exception e){
            e.printStackTrace();
        }
        String result = "";
        result = lifeInMinute / 60 +  " hour " + lifeInMinute % 60 + "minutes";
        return result;
    }

    public String getMovieTime(){
        int lifeInMinute = 0;
        try {
            double totalCurrent = this.cpuAwakeCurrent;
            if(mmanager.isBluetoothEnabled())
                totalCurrent += this.bluetoothCurrent;
            if(mmanager.isDataEnabled())
                totalCurrent += this.radioOnCurrent;
            if(mmanager.isWifiEnabled())
                totalCurrent += this.wifiOnCurrent;
            totalCurrent += this.audioCurrent + this.videoCurrent;
            totalCurrent += this.screenOnCurrent * mmanager.getBrightness() / 100;
            lifeInMinute = (int)(this.leftBattery / totalCurrent * 60);
        }catch (Exception e){
            e.printStackTrace();
        }
        String result = "";
        result = lifeInMinute / 60 +  " hour " + lifeInMinute % 60 + "minutes";
        return result;
    }
    public String getCellularTime(){
        int lifeInMinute = 0;
        try {
            double totalCurrent = this.cpuAwakeCurrent;
            if(mmanager.isBluetoothEnabled())
                totalCurrent += this.bluetoothCurrent;
            if(mmanager.isDataEnabled())
                totalCurrent += this.radioActiveCurrent;
            if(mmanager.isWifiEnabled())
                totalCurrent += this.wifiOnCurrent;
            totalCurrent += this.screenOnCurrent * mmanager.getBrightness() / 100;
            lifeInMinute = (int)(this.leftBattery / totalCurrent * 60);
        }catch (Exception e){
            e.printStackTrace();
        }
        String result = "";
        result = lifeInMinute / 60 +  " hour " + lifeInMinute % 60 + "minutes";
        return result;
    }

    public String getMusicTime(){
        int lifeInMinute = 0;
        try {
            double totalCurrent = this.cpuAwakeCurrent;
            if(mmanager.isBluetoothEnabled())
                totalCurrent += this.bluetoothCurrent;
            if(mmanager.isDataEnabled())
                totalCurrent += this.radioOnCurrent;
            if(mmanager.isWifiEnabled())
                totalCurrent += this.wifiOnCurrent;
            totalCurrent += this.audioCurrent;
            lifeInMinute = (int)(this.leftBattery / totalCurrent * 60);
        }catch (Exception e){
            e.printStackTrace();
        }
        String result = "";
        result = lifeInMinute / 60 +  " hour " + lifeInMinute % 60 + "minutes";
        return result;
    }

    public String getGamingTime(){
        int lifeInMinute = 0;
        try {
            double totalCurrent = this.cpuActiveCurrent;
            if(mmanager.isBluetoothEnabled())
                totalCurrent += this.bluetoothCurrent;
            if(mmanager.isDataEnabled())
                totalCurrent += this.radioOnCurrent;
            if(mmanager.isWifiEnabled())
                totalCurrent += this.wifiOnCurrent;
            totalCurrent += this.screenOnCurrent * mmanager.getBrightness() / 100;
            totalCurrent += this.audioCurrent += this.videoCurrent;
            lifeInMinute = (int)(this.leftBattery / totalCurrent * 60);
        }catch (Exception e){
            e.printStackTrace();
        }
        String result = "";
        result = lifeInMinute / 60 +  " hour " + lifeInMinute % 60 + "minutes";
        return result;
    }
    public String getPhoneTime(){
        int lifeInMinute = 0;
        try {
            double totalCurrent = this.cpuAwakeCurrent;
            if(mmanager.isBluetoothEnabled())
                totalCurrent += this.bluetoothCurrent;
            if(mmanager.isDataEnabled())
                totalCurrent += this.radioActiveCurrent;
            if(mmanager.isWifiEnabled())
                totalCurrent += this.wifiOnCurrent;
            lifeInMinute = (int)(this.leftBattery / totalCurrent * 60);
        }catch (Exception e){
            e.printStackTrace();
        }
        String result = "";
        result = lifeInMinute / 60 +  " hour " + lifeInMinute % 60 + "minutes";
        return result;
    }
}