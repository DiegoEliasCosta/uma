package diego.ufu.workloadoutput.calculus;

import java.util.List;

import diego.ufu.workloadoutput.bean.MallocBean;
import diego.ufu.workloadoutput.main.MallocInfo;

/**
 * Calculus used to get the frequency of each alloc operation
 *  
 * @author Diego Costa
 *
 */
public class FrequencyOperationCalculus implements CalculusModule {

	private static final String SEPARATOR = ";";

	@Override
	public String getHeader() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("# malloc()" + SEPARATOR)
		  	  .append("# calloc()" + SEPARATOR)
		      .append("# realloc()" + SEPARATOR)
		      .append("% malloc()" + SEPARATOR)
		      .append("% calloc()" + SEPARATOR)
		      .append("% realloc()" + SEPARATOR);
		
		return buffer.toString();
	}

	@Override
	public StringBuilder calculate(MallocInfo mallInfo) {
		
		int numberOfCalloc = 0;
		int numberOfMalloc = 0;
		int numberOfRealloc = 0;
		
		float percMalloc = 0;
		float percCalloc = 0;
		float percRealloc = 0;
		
		int numberOfAlloc;
		
		List<MallocBean> mallocList = mallInfo.getMallocList();
		numberOfAlloc = mallocList.size();
		
		for(MallocBean bean : mallocList) {
			switch(bean.getOperation()) {
			case MALLOC: 
				numberOfMalloc++; 
				break;
			case CALLOC:
				numberOfCalloc++;
				break;
			case REALLOC:
				numberOfRealloc++;
			}
		}
		
		percCalloc = numberOfCalloc / (float) numberOfAlloc * 100;
		percMalloc = numberOfMalloc / (float) numberOfAlloc * 100;
		percRealloc = numberOfRealloc / (float) numberOfAlloc * 100;
		
		StringBuilder buffer = new StringBuilder();
		buffer.append(numberOfMalloc + SEPARATOR)
		  .append(numberOfCalloc + SEPARATOR)
		  .append(numberOfRealloc + SEPARATOR)
		  .append(percMalloc + SEPARATOR)
		  .append(percCalloc + SEPARATOR)
		  .append(percRealloc + SEPARATOR);
		
		return buffer;
	}

}
