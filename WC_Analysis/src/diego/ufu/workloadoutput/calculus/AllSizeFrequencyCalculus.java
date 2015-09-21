package diego.ufu.workloadoutput.calculus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import diego.ufu.workloadoutput.bean.MallocBean;
import diego.ufu.workloadoutput.main.MallocInfo;

/**
 * Calculus used to calculate the frequency of ALL allocated sizes.
 * If you want to check the frequencies into a category check {@link SizeFrequencyCalculus}
 * 
 * @author Diego Costa
 *
 */
public class AllSizeFrequencyCalculus implements CalculusModule {

	private static final String NEW_LINE = "\n";
	private static final String SEPARATOR = ";";

	@Override
	public String getHeader() {
		return "Size" + SEPARATOR + "Frequency" + NEW_LINE;
	}

	@Override
	public StringBuilder calculate(MallocInfo mallocByRep) {
		
		
		Map<Integer, Integer> sizesByFrequency = new HashMap<>();
		
		
		for(MallocBean malloc : mallocByRep.getMallocList()) {
			
			Integer size = malloc.getSize();
			Integer cont = sizesByFrequency.get(size);
			
			if(cont == null) {
				sizesByFrequency.put(size, 1);
			} else {
				sizesByFrequency.put(size, ++cont);
			}
		}
		
		StringBuilder builder = new StringBuilder();
		
		long accum = 0;
		for(int i = 0; i < 2048; i ++) {
			
			Integer qty = sizesByFrequency.get(i);
			
			if(qty != null) {
				accum += qty;
			}
			
			builder.append(i)
				.append(SEPARATOR)
				.append(accum)
				.append(NEW_LINE);

		}
		
		return builder;
	}

	private Integer getMaxSize(Map<Integer, Integer> sizesByFrequency) {
		
		Set<Integer> keySet = sizesByFrequency.keySet();

		// Sort
		TreeSet<Integer> treeSet = new TreeSet<>(keySet);
		return treeSet.pollLast();
		
	}

}
