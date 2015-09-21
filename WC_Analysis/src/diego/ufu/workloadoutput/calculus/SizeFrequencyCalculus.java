package diego.ufu.workloadoutput.calculus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import diego.ufu.workloadoutput.bean.MallocBean;
import diego.ufu.workloadoutput.bean.SizeCategory;
import diego.ufu.workloadoutput.calculus.MostAllocatedControl.AllocationFrequency;
import diego.ufu.workloadoutput.main.MallocInfo;

/**
 * Calculates the frequency of allocated sizes that falls into 
 * a specific category
 * 
 * @author Diego Costa
 *
 */
public class SizeFrequencyCalculus implements CalculusModule {

	private static final String SEPARATOR = ";";
	
	private static final int numberOfMostAllocation = 10;

	@Override
	public String getHeader() {
		
		StringBuffer buffer = new StringBuffer();
		
		//buffer.append("Quantidade por Tamanhos (Tamanho=#) " + SEPARATOR)
		//.append("Distribuicao Frequencia dos Tamanhos (Tamanho=%)" + SEPARATOR)
		//buffer.append("Distribuição por Categoria (Categoria=%)" + SEPARATOR)
		buffer.append("Frequencia - C1" + SEPARATOR)
		.append("Frequencia - C2" + SEPARATOR)
		.append("Frequencia - C3" + SEPARATOR)
		.append("Frequencia - C4" + SEPARATOR)
		.append("Frequencia - C5" + SEPARATOR)
		.append("Frequencia - C6" + SEPARATOR)
		.append("Tamanhos Diferentes - C1" + SEPARATOR)
		.append("Tamanhos Diferentes - C2" + SEPARATOR)
		.append("Tamanhos Diferentes - C3" + SEPARATOR)
		.append("Tamanhos Diferentes - C4" + SEPARATOR)
		.append("Tamanhos Diferentes - C5" + SEPARATOR)
		.append("Tamanhos Diferentes - C6" + SEPARATOR);
		
		for(int i = 1; i <= numberOfMostAllocation; i++) {
			buffer.append(i + "º Tamanho mais alocado" + SEPARATOR)
			.append(i + "º Frequencia" + SEPARATOR);
		}

		buffer.append("Número de Tamanhos Diferentes" + SEPARATOR);
		
		return buffer.toString();
	}

	@Override
	public StringBuilder calculate(MallocInfo mallocInfo) {
		
		int numberOfAllocations = 0;
		
		Map<Integer, List<MallocBean>> mallocBySize = new HashMap<>();
		List<MallocBean> mallocList = mallocInfo.getMallocList();
		numberOfAllocations = mallocList.size();
		
		for(MallocBean mallocBean : mallocList) {
			Integer size = mallocBean.getSize();
			List<MallocBean> beanList = mallocBySize.get(size); 
			
			if(beanList == null) {
				List<MallocBean> list = new ArrayList<>();
				list.add(mallocBean);
				mallocBySize.put(size, list);
			} else {
				beanList.add(mallocBean);
				mallocBySize.put(size, beanList);
			}
			
		}
		
		Object[] sortedKeys = mallocBySize.keySet().toArray();
		Arrays.sort(sortedKeys);
		
		StringBuilder buffer = new StringBuilder();
		
		// # 
		//printMallocNumberBySize(buffer, mallocBySize, sortedKeys);
		
		// %
		//printMallocPercentageBySize(buffer, mallocBySize, sortedKeys, numberOfAllocations);
		
		// Category
		printCategoryBySize(buffer, mallocBySize, sortedKeys, numberOfAllocations);
		
		// Most Allocated Sizes
		printMostAllocatedSize(buffer, mallocBySize, sortedKeys);
		
		// Number of Different Sizes
		printNumberOfDifferentSizes(buffer, mallocBySize);
		
		return buffer;
	}

	private void printNumberOfDifferentSizes(StringBuilder buffer,
			Map<Integer, List<MallocBean>> mallocBySize) {
		
		buffer.append(mallocBySize.keySet().size())
		.append(SEPARATOR);
		
	}

	private void printMostAllocatedSize(StringBuilder buffer,
			Map<Integer, List<MallocBean>> mallocBySize, Object[] sortedKeys) {

		MostAllocatedControl control = new MostAllocatedControl(numberOfMostAllocation);
		
		for(Integer size : mallocBySize.keySet()) {
			
			control.insertElement(size, mallocBySize.get(size).size());
			
		}
		
		List<AllocationFrequency> mostAllocatedSizes = control.getMostAllocatedSizes();
		
		for(AllocationFrequency allocFrequency : mostAllocatedSizes) {
			buffer.append(allocFrequency.getSize())
			  .append(SEPARATOR)
			  .append(allocFrequency.getFrequency())
			  .append(SEPARATOR);
		}
		
		
		
		
	}

	private void printCategoryBySize(StringBuilder buffer,
			Map<Integer, List<MallocBean>> mallocBySize, Object[] sortedKeys,
			int numberOfAllocations) {
		
		Map<SizeCategory, Integer> countBySize = new HashMap<>();
		Map<SizeCategory, Integer> numberOfSizeByCategory = new HashMap<>();
		
		for(Object key : sortedKeys) {
			  Integer size = (Integer) key;
			  SizeCategory cat = SizeCategory.findCategory(size);
			  
			  Integer number = mallocBySize.get(key).size();
			  Integer count = countBySize.get(cat);
			  if(count == null) {
				  countBySize.put(cat, number);
				  numberOfSizeByCategory.put(cat, 1);
			  } else {
				  countBySize.put(cat, count + number);
				  numberOfSizeByCategory.put(cat, (numberOfSizeByCategory.get(cat) + 1));
			  }
		}
		
		// Count By Size
		Object[] sizeC = countBySize.keySet().toArray();
		Arrays.sort(sizeC);
		
		for(Object cat : sizeC) {
			buffer.append(countBySize.get(cat))
				  .append(SEPARATOR);
		}
		
		// Number of Different Sizes by Category
		Object[] categories = numberOfSizeByCategory.keySet().toArray();
		Arrays.sort(categories);
		
		for(Object cat : categories) {
			buffer.append(numberOfSizeByCategory.get(cat))
				  .append(SEPARATOR);
		}
		
	}

	@SuppressWarnings("unused")
	private void printMallocPercentageBySize(StringBuilder buffer,
			Map<Integer, List<MallocBean>> mallocBySize, Object[] sortedKeys, int numberOfAllocations) {
		
		buffer.append("{");
		for(Object key : sortedKeys) {
			  buffer.append(key) 
			        .append("=")
			        .append(frequency(mallocBySize.get(key), numberOfAllocations))
			        .append(", ");
		}
		buffer.append("}" + SEPARATOR);
		
	}

	@SuppressWarnings("unused")
	private void printMallocNumberBySize(StringBuilder buffer,
			Map<Integer, List<MallocBean>> mallocBySize, Object[] keys) {
		
		for(Object key : keys) {
			  buffer.append(key + "=" + mallocBySize.get(key).size() + ", ");
		}
		buffer.append("}" + SEPARATOR);
	}
	


	private String frequency(int count, int numberOfAllocations) {
		return "" + (count / (float) numberOfAllocations * 100);
	}

	private String frequency(List<MallocBean> beanList, int numberOfAllocations) {
		return frequency(beanList.size(), numberOfAllocations);
	}

}
