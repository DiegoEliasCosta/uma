package diego.ufu.workloadoutput.bean;

/**
 * Bean that stores the a operation that contains on memory.
 * @author Diego Costa
 *
 */
public class AllocBean {
	
	long time;
	
	String memoryID;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getMemoryID() {
		return memoryID;
	}

	public void setMemoryID(String memoryID) {
		this.memoryID = memoryID;
	}
	
}
