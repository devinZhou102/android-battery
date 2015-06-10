package cn.edu.pkusz.battery.db;

public class CpuTimeEntry {
	public long time;
    public long value;
    public int pid;
    public CpuTimeEntry(int pid,long time, long value ) {
        this.time = time;
        this.value = value;
        this.pid = pid;
    }

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public float getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}
    
    
}
