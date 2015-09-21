package diego.ufu.workloadoutput.calculus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import diego.ufu.workloadoutput.bean.FreeBean;
import diego.ufu.workloadoutput.bean.MallocBean;
import diego.ufu.workloadoutput.bean.SizeCategory;
import diego.ufu.workloadoutput.main.MallocInfo;



/**
 * The Old implementation to retrieve the retention time between
 * allocation and free
 * Use {@link SmartTimeRetentionCalculus} instead
 * @author Diego Costa
 *
 */
@Deprecated
public class TimeRetentionCalculus implements CalculusModule {

	private static final String SEPARATOR = ";";
	
	@Override
	public String getHeader() {
		StringBuilder buff = new StringBuilder();
		
		buff.append("Tempo de Retenção Médio" + SEPARATOR);
		buff.append("Tempo de Retencao por Categoria de Tamanho (Tamanho=#)");
		
		return buff.toString();
	}

	@Override
	public StringBuilder calculate(MallocInfo mallocByRep) {
		
		List<MallocBean> mallocList = new ArrayList<>(mallocByRep.getMallocList());
		MallocBean lastMalloc = mallocList.get(mallocList.size() - 1);
		List<FreeBean> freeList = new ArrayList<>(mallocByRep.getFreeList());
		
		Map<SizeCategory, List<Long>> timeRetentionByCategory = new HashMap<>();
		long timeRetentionTotal = 0L;
		int count = 0;
		
		for(MallocBean malloc : mallocList) {
			String memoryID = malloc.getMemoryID();
			long timeRetention = 0;
			
			boolean freeFound = false;
			
			for(FreeBean free : freeList) {
				// malloc.id == free.id AND free.time > malloc.time
				if(free.getMemoryID().equals(memoryID) && (free.getTime() > malloc.getTime())) {
					freeList.remove(free);
					timeRetention += free.getTime() - malloc.getTime();
					freeFound = true;
					break;
				}
			}
			
			// If free() not found, use the last malloc as the end of the execution
			if(!freeFound) {
				timeRetention += lastMalloc.getTime() - malloc.getTime(); 
			}
			

			
			// Add to the store structures
			timeRetentionTotal += timeRetention;
			count++;
			
			SizeCategory category = SizeCategory.findCategory(malloc.getSize());
			List<Long> timeRetentionList = timeRetentionByCategory.get(category);
			if(timeRetentionList == null) {
				timeRetentionList = new ArrayList<>();
				timeRetentionByCategory.put(category, timeRetentionList);
			}
			timeRetentionList.add(timeRetention);
		
		}
		
		StringBuilder buff = new StringBuilder();
		
		buff.append((timeRetentionTotal / (float) count) + SEPARATOR);
		
		// By Retention Category
		Object[] keys = timeRetentionByCategory.keySet().toArray();
		buff.append("{");
		for(Object key : keys) {
			  buff.append(key + "=" + getTimeRetentionAvg(timeRetentionByCategory.get(key)) + ", ");
		}
		buff.append("}" + SEPARATOR);
		
		return buff;
	}
	
	private String getTimeRetentionAvg(List<Long> list) {
		
		long total = 0L;
		for(Long e : list) {
			total += e;
		}
		double d = total / 1000000000.0; // to seconds
		return "" + (d / (float) list.size());
	}

}
