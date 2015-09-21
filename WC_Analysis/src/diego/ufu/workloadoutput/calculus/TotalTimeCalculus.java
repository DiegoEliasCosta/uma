package diego.ufu.workloadoutput.calculus;

import java.util.List;

import diego.ufu.workloadoutput.bean.FreeBean;
import diego.ufu.workloadoutput.bean.MallocBean;
import diego.ufu.workloadoutput.main.MallocInfo;

/**
 * Calculate the time spent in the experiment
 * 
 * @author Diego Costa
 *
 */
public class TotalTimeCalculus implements CalculusModule {

	private static final String SEPARATOR = ";";
	
	@Override
	public String getHeader() {
		
		return "Tempo Total do Experimento" + SEPARATOR;
	}

	
	/**
	 * It calculates by getting the first and the last operation 
	 * and subtracting the time between both operations.
	 */
	@Override
	public StringBuilder calculate(MallocInfo mallocByRep) {
		
		Long firstTime = Long.MAX_VALUE;
		Long lastTime = Long.MIN_VALUE;
		
		List<MallocBean> mallocList = mallocByRep.getMallocList();
		List<FreeBean> freeList = mallocByRep.getFreeList();
		
		// This can be done in a easier way by sorting the data O(n * logn)
		// However is more expensive than just searching O(n)
		
		// Malloc
		for(MallocBean malloc : mallocList) {
			if(firstTime > malloc.getTime()){
				firstTime = malloc.getTime();
			}
			
			if(lastTime < malloc.getTime()) {
				lastTime = malloc.getTime();
			}
		}
		
		// Free
		for(FreeBean free : freeList) {
			if(firstTime > free.getTime()){
				firstTime = free.getTime();
			}
			
			if(lastTime < free.getTime()) {
				lastTime = free.getTime();
			}
		}
		
		StringBuilder buff = new StringBuilder();
		
		buff.append(lastTime - firstTime)
		.append(SEPARATOR);
		
		return buff;
	}

}
