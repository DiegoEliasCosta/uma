package diego.ufu.workloadoutput.calculus;

import diego.ufu.workloadoutput.main.MallocInfo;

public interface CalculusModule {

	/**
	 * Get the Header that will be written in the .csv file
	 * 
	 * @return
	 */
	String getHeader();
	
	/**
	 * DO the calculation of the module
	 * @param mallocByRep
	 * 		The Info retrieved by replication 
	 * @return
	 */
	StringBuilder calculate(MallocInfo mallocByRep);

}
