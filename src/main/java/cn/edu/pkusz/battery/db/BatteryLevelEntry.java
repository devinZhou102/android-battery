package cn.edu.pkusz.battery.db;

/**
 * Created by 陶世博 on 2015/6/2.
 */
public class BatteryLevelEntry {
    public long timestamp;
    public float level;
    public boolean isCharging;
    
    public BatteryLevelEntry(long timestamp, float level, boolean isCharging) {
        this.timestamp = timestamp;
        this.level = level;
        this.isCharging = isCharging;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BatteryLevelEntry that = (BatteryLevelEntry) obj;
        if (this.timestamp == that.timestamp) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new Long(timestamp).hashCode();
    }
}
