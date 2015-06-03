package cn.edu.pkusz.battery.db;

/**
 * Created by 陶世博 on 2015/6/2.
 */
public class TrafficAmountEntry {
    public long timestamp;
    public int amount;

    public TrafficAmountEntry(long timestamp, int amount) {
        this.timestamp = timestamp;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TrafficAmountEntry that = (TrafficAmountEntry) obj;
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
