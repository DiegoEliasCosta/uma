package diego.ufu.workloadoutput.calculus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Wrapper of sizes/frequencies for the frequency analysis
 * 
 * @author Diego Costa
 *
 */
public class MostAllocatedControl {
	
	/**
	 * Contains the size and their frequency
	 * @author Diego
	 *
	 */
	class AllocationFrequency {
		
		int frequency;
		int size;
		
		public AllocationFrequency(int frequency, int size) {
			super();
			this.frequency = frequency;
			this.size = size;
		}
		
		public int getFrequency() {
			return frequency;
		}
		public int getSize() {
			return size;
		}
		
	}

	List<AllocationFrequency> mostAllocatedSizes = new ArrayList<>(); 
	
	int limitTrace;
	
	public MostAllocatedControl(int nTrace) {
		this.limitTrace = nTrace;
		
	}
	
	public void insertElement(int size, int frequency) {
		
		mostAllocatedSizes.add(new AllocationFrequency(frequency, size));

	}
	
	public List<AllocationFrequency> getMostAllocatedSizes() {
		
		Collections.sort(mostAllocatedSizes, new Comparator<AllocationFrequency>() {

			@Override
			public int compare(AllocationFrequency o1, AllocationFrequency o2) {
				return o2.frequency - o1.frequency;
			}
		});
		
		return mostAllocatedSizes.subList(0, limitTrace);
		
	}
	
	
}
