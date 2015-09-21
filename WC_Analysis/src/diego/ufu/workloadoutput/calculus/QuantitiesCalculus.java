package diego.ufu.workloadoutput.calculus;

import java.util.List;

import diego.ufu.workloadoutput.bean.FreeBean;
import diego.ufu.workloadoutput.bean.MallocBean;
import diego.ufu.workloadoutput.main.MallocInfo;

/**
 * Calculus used to extract "quantities" information (see getHeader())
 * 	
 * @author Diego Costa
 *
 */
public class QuantitiesCalculus implements CalculusModule {
	
	private static final String ALREADY_DEALLOCATED = "(nil)";
	private static final String SEPARATOR = ";";
	
	@Override
	public StringBuilder calculate(MallocInfo mallInfo) {
		
		int numberOfAlloc = 0;
		int numberOfDealloc = 0;
		int numberofNullDealloc = 0;
		long totalOfMemoryAllocated = 0;
		float meanOfSizeAllocated;
		
		// Malloc
		List<MallocBean> mallocList = mallInfo.getMallocList();
		numberOfAlloc += mallocList.size();
		for(MallocBean mallocBean : mallocList){
			totalOfMemoryAllocated += mallocBean.getSize();
		}
		
		
		meanOfSizeAllocated = totalOfMemoryAllocated / (float) numberOfAlloc;
		
		// Free
		List<FreeBean> freeList = mallInfo.getFreeList();
		numberOfDealloc += freeList.size();
		for(FreeBean free : freeList) {
			if(free.getMemoryID().equals(ALREADY_DEALLOCATED)) {
				numberofNullDealloc++;
			}
		}
		
		
		StringBuilder bf = new StringBuilder();
		bf.append(numberOfAlloc + SEPARATOR)
		  .append(numberOfDealloc + SEPARATOR)
		  .append(numberofNullDealloc + SEPARATOR)
		  .append(calculatePercOfAllocWithoutDesalloc(numberOfAlloc, numberOfDealloc, numberofNullDealloc) + SEPARATOR)
		  .append(totalOfMemoryAllocated + SEPARATOR)
		  .append(meanOfSizeAllocated + SEPARATOR);
		
		return bf;
	}

	private String calculatePercOfAllocWithoutDesalloc(int numberOfAlloc,
			int numberOfDealloc, int numberofNullDealloc) {
		return "" + (numberOfAlloc - (numberOfDealloc - numberofNullDealloc)) / (float) numberOfAlloc * 100;
	}

	@Override
	public String getHeader() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("# de Alocacoes;")
			  .append("# de Desalocacoes;")
			  .append("# de Desalocações NULL;")
			  .append("% de alocações não desalocadas;")
			  .append("Total de Memoria Requisitada (bytes);")
			  .append("Tamanho Medio de Alocacao (bytes);");
		
		return buffer.toString();
	}

}
