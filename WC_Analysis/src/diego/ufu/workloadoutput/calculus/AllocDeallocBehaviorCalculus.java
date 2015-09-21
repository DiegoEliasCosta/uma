package diego.ufu.workloadoutput.calculus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import diego.ufu.workloadoutput.bean.AllocBean;
import diego.ufu.workloadoutput.bean.FreeBean;
import diego.ufu.workloadoutput.bean.MallocBean;
import diego.ufu.workloadoutput.bean.TimeComparator;
import diego.ufu.workloadoutput.main.MallocInfo;

/**
 * Calculus that extracts the info of Allocation and Deallocation
 * by ordering them by their time
 * 
 * Useful to analyze patterns in the behaviour of the alloc/dealloc 
 * 
 * @author Diego Costa
 *
 */
public class AllocDeallocBehaviorCalculus implements CalculusModule {

	private static final String SEPARATOR = ";";
	private static final Object NEWLINE = "\n";
	private static final int SEGREGATION_QTY = 10000;
	
	@Override
	public String getHeader() {
		return "Alloc/Dealloc Behavior" + SEPARATOR + "Malloc" + SEPARATOR + "Free";
	}

	@Override
	public StringBuilder calculate(MallocInfo mallocByRep) {

		List<MallocBean> mallocList = mallocByRep.getMallocList();
		List<FreeBean> freeList = mallocByRep.getFreeList();
		
		List<AllocBean> allocList = new ArrayList<>();
		
		allocList.addAll(mallocList);
		allocList.addAll(freeList);
		
		Collections.sort(allocList, new TimeComparator());
		
		
		int allocVsDealloc = 0;
		int cont = 0;
		int mallocCount = 0;
		int freeCount = 0;
		
		StringBuilder buff = new StringBuilder();
		
		for(AllocBean alloc : allocList) {
			
			int allocRow = 0;
			
			if(alloc instanceof MallocBean) {
				allocVsDealloc ++;
				mallocCount ++;
				allocRow++;
			}
			
			if(alloc instanceof FreeBean) {
				if(allocRow > 0) {
					allocRow = 0;
				}
				allocVsDealloc --;
				freeCount ++;
			}
			
			cont++;
			
			if(cont == SEGREGATION_QTY) {
				buff.append(allocVsDealloc)
				.append(SEPARATOR)
				.append(mallocCount)
				.append(SEPARATOR)
				.append(freeCount)
				.append(NEWLINE);
				
				cont = 0;
				mallocCount = 0;
				freeCount = 0;
			}
			
		}
		
		return buff;
	}

}
