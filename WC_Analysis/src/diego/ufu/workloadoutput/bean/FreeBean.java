package diego.ufu.workloadoutput.bean;


/**
 * Bean that stores the info of a single dealloc information.
 * 
 * @author Diego
 *
 */
public class FreeBean extends AllocBean {

	FreeOpEnum operation;
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public FreeOpEnum getOperation() {
		return operation;
	}

	public void setOperation(FreeOpEnum operation) {
		this.operation = operation;
	}

	public String getMemoryID() {
		return memoryID;
	}

	public void setMemoryID(String memoryID) {
		this.memoryID = memoryID;
	}
	
	@Override
	public String toString() {
		return "Time: " + time + " Op:" + operation + " MemoryID:" + memoryID;
	}
}
