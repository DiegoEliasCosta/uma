package diego.ufu.workloadoutput.calculus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.util.Pair;

import diego.ufu.workloadoutput.bean.AllocBean;
import diego.ufu.workloadoutput.bean.FreeBean;
import diego.ufu.workloadoutput.bean.MallocBean;
import diego.ufu.workloadoutput.bean.TimeComparator;
import diego.ufu.workloadoutput.main.MallocInfo;

public class AllocDeallocGroupCalculus implements CalculusModule {

	private static final String SEPARATOR = ";";
	
	@Override
	public String getHeader() {
		return "Alloc Media" + SEPARATOR + "Alloc Mediana" + SEPARATOR + "Alloc Moda" + SEPARATOR + "Alloc Moda (count)" + SEPARATOR + "Alloc Total Group" + SEPARATOR +
				"Dealloc Media" + SEPARATOR + "Dealloc Mediana" + SEPARATOR + "Dealloc Moda" + SEPARATOR + "Dealloc Moda (count)" + SEPARATOR + "Dealloc Total Group" + SEPARATOR;
	}

	@Override
	public StringBuilder calculate(MallocInfo mallocByRep) {

		List<MallocBean> mallocList = mallocByRep.getMallocList();
		List<FreeBean> freeList = mallocByRep.getFreeList();
		
		List<AllocBean> allocList = new ArrayList<>();
		
		allocList.addAll(mallocList);
		allocList.addAll(freeList);
		
		Collections.sort(allocList, new TimeComparator());
		
		List<Integer> allocGroupList = new ArrayList<>();
		List<Integer> deallocGroupList = new ArrayList<>();
		
		StringBuilder buff = new StringBuilder();
		
		int allocRow = 0;
		int deallocRow = 0;
		for(AllocBean alloc : allocList) {
			
			if(alloc instanceof MallocBean) {
				allocRow++;
				if(deallocRow > 0) {
					deallocGroupList.add(deallocRow);
					deallocRow = 0;
				}
			}
			
			if(alloc instanceof FreeBean) {
				deallocRow++;
				if(allocRow > 0) {
					allocGroupList.add(allocRow);
					allocRow = 0;
				}
			}
			
		}
		
		/*
		 * ALLOC 
		 */
		// Mean
		buff.append(String.format("%,2f", mean(allocGroupList)) + SEPARATOR);
		
		// Median
		buff.append(String.format("%,2f", median(allocGroupList)) + SEPARATOR);
		
		// Moda
		Pair<Integer, Integer> p = mode(allocGroupList);
		buff.append(p.getFirst() + SEPARATOR + p.getSecond() + SEPARATOR);
		
		// Total
		buff.append(allocGroupList.size() + SEPARATOR);
		
		/*
		 * DEALLOC 
		 */
		// Mean
		buff.append(String.format("%,2f", mean(deallocGroupList)) + SEPARATOR);
		
		// Median
		buff.append(String.format("%,2f", median(deallocGroupList)) + SEPARATOR);
		
		// Moda
		Pair<Integer, Integer> p2 = mode(deallocGroupList);
		buff.append(p2.getFirst() + SEPARATOR + p2.getSecond() + SEPARATOR);
		
		// Total
		buff.append(deallocGroupList.size() + SEPARATOR);
		
		return buff;
	}
	
	public static double mean(List<Integer> list) {
		
		long groupTotal = 0;
		for(Integer group : list) {
			groupTotal += group;
		}
		
		return groupTotal / (double) list.size();
		
	}
	
	public static double median(List<Integer> list) {
		
	    int middle = list.size() / 2;
	    Collections.sort(list); // sorted
	    
	    if (list.size() % 2 == 1) {
	        return list.get(middle);
	    } else {
	        return (list.get(middle - 1) + list.get(middle)) / 2.0;
	    }
	}
	
	public static Pair<Integer, Integer> mode(List<Integer> aList) {

		Integer count;
	    Map<Integer, Integer> countByGroup = new HashMap<Integer, Integer>();
	    
	    for (Integer a : aList) {
	        
	    	Integer c = countByGroup.get(a);
	    	
	    	if(c == null) {
	    		countByGroup.put(a, 1);
	    		
	    	} else {
	    		countByGroup.put(a, ++c);
	    	}
	    }
	    
	    count = 0;
	    int maxValue = 0;
	    
	    for(Integer group : countByGroup.keySet()) {
	    	
	    	Integer c = countByGroup.get(group);
	    	if(c > count) {
	    		count = c;
	    		maxValue = group;
	    	}
	    }

	    return new Pair<>(maxValue, count);
	}

}
