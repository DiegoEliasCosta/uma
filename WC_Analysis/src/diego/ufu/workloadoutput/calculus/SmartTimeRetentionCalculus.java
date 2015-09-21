package diego.ufu.workloadoutput.calculus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import diego.ufu.workloadoutput.bean.FreeBean;
import diego.ufu.workloadoutput.bean.MallocBean;
import diego.ufu.workloadoutput.bean.SizeCategory;
import diego.ufu.workloadoutput.bean.TimeComparator;
import diego.ufu.workloadoutput.bean.TimeRetentionCategory;
import diego.ufu.workloadoutput.main.MallocInfo;

/**
 * Class that retrieves the Retention time and classifies
 * them into the {@link TimeRetentionCategory}
 * 
 * Retention time of an allocation A and its deallocation D is equal to
 * 						D.time - A.time
 * 
 * @author Diego Costa
 *
 */
public class SmartTimeRetentionCalculus implements CalculusModule {
	
	private static final int BILLION = 1000000000;
	private static final String SEPARATOR = ";";

	@Override
	public String getHeader() {
		StringBuilder buff = new StringBuilder();
		
		//buff.append("Tempo de Retencao por Categoria de Tamanho (Tamanho=#)" + SEPARATOR);
		buff.append("Tempo Retenção Médio - C1" + SEPARATOR)
		.append("Variância Tempo Retenção - C1" + SEPARATOR)
		.append("Desvio Padrão Tempo Retenção - C1" + SEPARATOR)
		.append("Mínimo Tempo Retenção - C1" + SEPARATOR)
		.append("Máximo Tempo Retenção - C1" + SEPARATOR)
		.append("Qtd de Alocações Breves - C1" + SEPARATOR)
		
		.append("Tempo Retenção Médio - C2" + SEPARATOR)
		.append("Variância Tempo Retenção - C2" + SEPARATOR)
		.append("Desvio Padrão Tempo Retenção - C2" + SEPARATOR)
		.append("Mínimo Tempo Retenção - C2" + SEPARATOR)
		.append("Máximo Tempo Retenção - C2" + SEPARATOR)
		.append("Qtd de Alocações Breves - C2" + SEPARATOR)
		
		.append("Tempo Retenção Médio - C3" + SEPARATOR)
		.append("Variância Tempo Retenção - C3" + SEPARATOR)
		.append("Desvio Padrão Tempo Retenção - C3" + SEPARATOR)
		.append("Mínimo Tempo Retenção - C3" + SEPARATOR)
		.append("Máximo Tempo Retenção - C3" + SEPARATOR)
		.append("Qtd de Alocações Breves - C3" + SEPARATOR)
		
		.append("Tempo Retenção Médio - C4" + SEPARATOR)
		.append("Variância Tempo Retenção - C4" + SEPARATOR)
		.append("Desvio Padrão Tempo Retenção - C4" + SEPARATOR)
		.append("Mínimo Tempo Retenção - C4" + SEPARATOR)
		.append("Máximo Tempo Retenção - C4" + SEPARATOR)
		.append("Qtd de Alocações Breves - C4" + SEPARATOR)
		
		.append("Tempo Retenção Médio - C5" + SEPARATOR)
		.append("Variância Tempo Retenção - C5" + SEPARATOR)
		.append("Desvio Padrão Tempo Retenção - C5" + SEPARATOR)
		.append("Mínimo Tempo Retenção - C5" + SEPARATOR)
		.append("Máximo Tempo Retenção - C5" + SEPARATOR)
		.append("Qtd de Alocações Breves - C5" + SEPARATOR)
		
		.append("Tempo Retenção Médio - C6" + SEPARATOR)
		.append("Variância Tempo Retenção - C6" + SEPARATOR)
		.append("Desvio Padrão Tempo Retenção - C6" + SEPARATOR)
		.append("Mínimo Tempo Retenção - C6" + SEPARATOR)
		.append("Máximo Tempo Retenção - C6" + SEPARATOR)
		.append("Qtd de Alocações Breves - C6" + SEPARATOR)
		
		 .append(TimeRetentionCategory.TR_C1.getSup() + SEPARATOR)
		 .append(TimeRetentionCategory.TR_C2.getSup() + SEPARATOR)
		 .append(TimeRetentionCategory.TR_C3.getSup() + SEPARATOR)
		 .append(TimeRetentionCategory.TR_C4.getSup() + SEPARATOR)
		 .append(TimeRetentionCategory.TR_C5.getSup() + SEPARATOR)
		 .append(TimeRetentionCategory.TR_C6.getSup() + SEPARATOR)
		 .append(TimeRetentionCategory.TR_C7.getSup() + SEPARATOR)
		 .append(TimeRetentionCategory.TR_C8.getSup() + SEPARATOR)
		 .append(TimeRetentionCategory.TR_C9.getSup() + SEPARATOR)
		 .append(TimeRetentionCategory.TR_C10.getSup() + SEPARATOR)
		 .append(TimeRetentionCategory.TR_C11.getSup() + SEPARATOR)
		 .append(TimeRetentionCategory.TR_C12.getSup() + SEPARATOR)
		 
		.append("Grupo de Alocação Médio" + SEPARATOR);
		
		return buff.toString();
	}
	

	
	@Override
	public StringBuilder calculate(MallocInfo mallocByRep) {
		
		List<MallocBean> mallocList = mallocByRep.getMallocList();
		List<FreeBean> freeList = mallocByRep.getFreeList();
		
		MallocBean lastMalloc = mallocList.get(mallocList.size() - 1);
		
		// Memory By MEMORY ID
		Map<String, List<MallocBean>> mallocByMemoryID = new HashMap<>();
		Map<String, List<FreeBean>> freeByMemoryID = new HashMap<>();
		
		// TimeRetention By CATEGORY
		Map<SizeCategory, List<Long>> timeRetentionBySizeCategory = new HashMap<>();
		// TimeRetention By TIMERETENTION CATEGORY
		Map<TimeRetentionCategory, Long> timeRetentionByCategory = new HashMap<>();
		
		// 1 - Fill the allocByMemoryID (Malloc)
		fillMallocByMemoryID(mallocByMemoryID, mallocList);
		fillFreeByMemoryID(freeByMemoryID, freeList);
		
		// 2 - Find the Retention and put in the SIZE Category
		calculateTimeRetention(mallocByMemoryID, freeByMemoryID, timeRetentionBySizeCategory, timeRetentionByCategory, lastMalloc);
		
		StringBuilder buff = new StringBuilder();
		
		// By Retention Category
		Object[] keys = timeRetentionBySizeCategory.keySet().toArray();
		Arrays.sort(keys);
		for(Object key : keys) {
			  buff.append(calculateAndPrintStatistics(timeRetentionBySizeCategory.get(key)));
		}
		
		// By Time Retention Category
		Object[] keysTR = timeRetentionByCategory.keySet().toArray();
		Arrays.sort(keysTR);
		for(Object key : keysTR) {
			  buff.append(timeRetentionByCategory.get(key) + SEPARATOR);
		}
		
		return buff;
	}
	


	private String calculateAndPrintStatistics(List<Long> list) {
		
		long sum = 0L;
		int size = list.size();
		
		long min = Long.MAX_VALUE;
		long max = Long.MIN_VALUE;
		int qtyShortTimeAllocations = 0;
		
		// Mean
		for(Long e : list) {
			sum += e;
			if(e < min) min = e;
			if(e > max) max = e;
			if(e < BILLION) qtyShortTimeAllocations++; // Less than one second
		} 
		double mean = sum / (double) list.size(); // average
		
		// Variance
		double temp = 0;
        for(Long e : list)
            temp += (mean - e)*( mean - e);
        double variance = temp / (double) size;
        
        // Std Deviation
        double stdDeviation = Math.sqrt(variance);
		
		// to Seconds
        mean /= BILLION;
        variance /= BILLION;
        stdDeviation /= BILLION;
        min /= BILLION;
        max /= BILLION;
		
		return mean + SEPARATOR + variance + SEPARATOR + stdDeviation + SEPARATOR + 
				min + SEPARATOR + max + SEPARATOR + qtyShortTimeAllocations + SEPARATOR;

	}

	private void calculateTimeRetention(
			Map<String, List<MallocBean>> mallocByMemoryID,
			Map<String, List<FreeBean>> freeByMemoryID,
			Map<SizeCategory, List<Long>> timeRetentionBySizeCategory,
			Map<TimeRetentionCategory, Long> timeRetentionByCategory, MallocBean lastMalloc) {
		
		
		initializeTimeRetention(timeRetentionByCategory);

		
		// 1 - FOR EACH MEMORYID USED
		for(String mID : mallocByMemoryID.keySet()) {
			
			List<MallocBean> mallocList = mallocByMemoryID.get(mID);
			List<FreeBean> freeList = freeByMemoryID.get(mID);
			
			//Sorting
			Collections.sort(mallocList, new TimeComparator());
			if(freeList != null) Collections.sort(freeList, new TimeComparator());
			
			// 2 - FOR EACH MALLOC/FREE IN A MEMORYID
			int mSize = mallocList.size();
			for(int i = 0; i < mSize; i++) {
				MallocBean malloc = mallocList.get(i);
				
				// 3 - CALCULATE TIME RETENTION
				long timeRetention = 0;
				FreeBean free = null;
				
				if(freeList != null) {
					
					if(i >= freeList.size()) { // 
						continue; // Don't consider the not deallocated objects 
						//timeRetention = lastMalloc.getTime() - malloc.getTime();
					} else {
						free = freeList.get(i);
						timeRetention = free.getTime() - malloc.getTime();
					}
					
				} else {
					timeRetention = lastMalloc.getTime() - malloc.getTime();
				}
				
				// 4 - ADD TIME RETENTION INTO CATEGORY
				insertTimeRetention(timeRetention, timeRetentionBySizeCategory, malloc.getSize()); // insert
				insertTimeRetention(timeRetention, timeRetentionByCategory);
				
			}
			
		}
		
	}


	private void initializeTimeRetention(
			Map<TimeRetentionCategory, Long> timeRetentionByCategory) {
		
		timeRetentionByCategory.put(TimeRetentionCategory.TR_C1, 0L);
		timeRetentionByCategory.put(TimeRetentionCategory.TR_C2, 0L);
		timeRetentionByCategory.put(TimeRetentionCategory.TR_C3, 0L);
		timeRetentionByCategory.put(TimeRetentionCategory.TR_C4, 0L);
		timeRetentionByCategory.put(TimeRetentionCategory.TR_C5, 0L);
		timeRetentionByCategory.put(TimeRetentionCategory.TR_C6, 0L);
		timeRetentionByCategory.put(TimeRetentionCategory.TR_C7, 0L);
		timeRetentionByCategory.put(TimeRetentionCategory.TR_C8, 0L);
		timeRetentionByCategory.put(TimeRetentionCategory.TR_C9, 0L);
		timeRetentionByCategory.put(TimeRetentionCategory.TR_C10, 0L);
		timeRetentionByCategory.put(TimeRetentionCategory.TR_C11, 0L);
		timeRetentionByCategory.put(TimeRetentionCategory.TR_C12, 0L);
		
		
	}


	private void insertTimeRetention(long timeRetention,
			Map<TimeRetentionCategory, Long> timeRetentionByCategory) {
		
		TimeRetentionCategory category = TimeRetentionCategory.findCategory(timeRetention);
		
		if(timeRetentionByCategory.containsKey(category)) {
			long count = timeRetentionByCategory.get(category);
			timeRetentionByCategory.put(category, ++count);
		} else {
			timeRetentionByCategory.put(category, 1L);
		}
		
	}


	private void insertTimeRetention(long timeRetention,
			Map<SizeCategory, List<Long>> timeRetentionByCategory, int size) {
		
		if (timeRetentionByCategory.containsKey(SizeCategory.findCategory(size))) {
			List<Long> timeRetentionList = timeRetentionByCategory.get(SizeCategory.findCategory(size));
			timeRetentionList.add(timeRetention);
			
		} else {
			List<Long> timeRetentionList = new ArrayList<>();
			timeRetentionList.add(timeRetention);
			timeRetentionByCategory.put(SizeCategory.findCategory(size), timeRetentionList);
		}
		
	}
	

	private void fillFreeByMemoryID(Map<String, List<FreeBean>> allocByMemoryID,
			List<FreeBean> freeList) {
		
		for(FreeBean free : freeList) {
			
			String id = free.getMemoryID();
			
			if(allocByMemoryID.containsKey(id)){
				List<FreeBean> aList = allocByMemoryID.get(id);
				aList.add(free);
			} else {
				List<FreeBean> aList = new ArrayList<>();
				aList.add(free);
				allocByMemoryID.put(id, aList);
			}
			
		}
		
	}

	
	private void fillMallocByMemoryID(Map<String, List<MallocBean>> allocByMemoryID,
			List<MallocBean> mallocList) {
		
		for(MallocBean malloc : mallocList) {
			
			String id = malloc.getMemoryID();
			
			if(allocByMemoryID.containsKey(id)){
				List<MallocBean> aList = allocByMemoryID.get(id);
				aList.add(malloc);
			} else {
				List<MallocBean> aList = new ArrayList<>();
				aList.add(malloc);
				allocByMemoryID.put(id, aList);
			}
			
		}
		
	}

}
