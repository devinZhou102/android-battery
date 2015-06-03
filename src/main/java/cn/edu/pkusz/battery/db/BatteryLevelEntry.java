package cn.edu.pkusz.battery.db;

/**
 * Created by 陶世博 on 2015/6/2.
 */
public class BatteryLevelEntry {
    public long timestamp;
    public float level;

    public BatteryLevelEntry(long timestamp, float level) {
        this.timestamp = timestamp;
        this.level = level;
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
