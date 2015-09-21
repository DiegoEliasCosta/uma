package diego.ufu.workloadoutput.bean;


/**
 * Bean that stores the alloc information.
 * 
 * size;time;op;memoryID
 * 
 * @author Diego
 *
 */
public class MallocBean extends AllocBean {
	
	MallocOpEnum operation;
	
	int size;
	
	public MallocOpEnum getOperation() {
		return operation;
	}

	public void setOperation(MallocOpEnum operation) {
		this.operation = operation;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	@Override
	public String toString() {
		
		return "Time: " + time + " Op:" + operation + " Size:" + size + " MemoryID:" + memoryID;
	}
	
}
