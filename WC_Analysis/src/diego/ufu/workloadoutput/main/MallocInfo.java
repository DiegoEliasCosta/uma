package diego.ufu.workloadoutput.main;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import diego.ufu.workloadoutput.bean.FreeBean;
import diego.ufu.workloadoutput.bean.FreeOpEnum;
import diego.ufu.workloadoutput.bean.MallocBean;
import diego.ufu.workloadoutput.bean.MallocOpEnum;



/**
 * Keeps the information for a entire replication test.
 * 
 * @author Diego
 *
 */
public class MallocInfo {
	
	List<MallocBean> mallocList = new ArrayList<>();
	
	List<FreeBean> freeList = new ArrayList<>();
	
	private static final String MALLOC_SEPARATOR = ";";
	private static final String FREE_SEPARATOR = " ";
	

	public void populateMalloc(List<String> mallocFile, String fileName) {
		
		int errorsCount = 0;
		
		for(String s : mallocFile) {
			
			MallocBean mallocBean = new MallocBean();
			
			try {
				mallocList.add(parseMallocBean(s, mallocBean));
			} catch (NumberFormatException e) { 
				errorsCount++;
			}
			
		}
		
		printErrors(fileName, errorsCount);
		
	}

	private void printErrors(String fileName, int errorsCount) {
		if(errorsCount > 0) {
			System.out.print(String.format("Errors in %s = %d", fileName, errorsCount ));
		}
	}

	public void populateFree(List<String> freeFile, String fileName) {
		
		int errorsCount = 0;
		
		for(String s : freeFile) {
			FreeBean freeBean = new FreeBean();
			
			try {
				freeList.add(parseFreeBean(s, freeBean));
			} catch (NumberFormatException e) { 
				errorsCount++;
			} 
		}
		
		printErrors(fileName, errorsCount);
		
	}
	
	private MallocBean parseMallocBean(String s, MallocBean mallocBean) {
		// size;time;op;memoryID
		String lineArray[] = s.split(MALLOC_SEPARATOR);
					
		mallocBean.setSize(Integer.parseInt(lineArray[0]));
		mallocBean.setTime(Long.parseLong(lineArray[1]));
		mallocBean.setOperation(MallocOpEnum.parse(lineArray[2]));
		mallocBean.setMemoryID(lineArray[3]);
		
		return mallocBean;
	}


	private FreeBean parseFreeBean(String s, FreeBean freeBean) {
		// time;op;memoryID
		String lineArray[] = s.split(FREE_SEPARATOR);
		
		freeBean.setTime(Long.parseLong(lineArray[0]));
		freeBean.setOperation(FreeOpEnum.parse(lineArray[1]));
		freeBean.setMemoryID(lineArray[2]);
		
		return freeBean;
	}

	public List<MallocBean> getMallocList() {
		return mallocList;
	}

	public void setMallocList(List<MallocBean> mallocList) {
		this.mallocList = mallocList;
	}

	public List<FreeBean> getFreeList() {
		return freeList;
	}

	public void setFreeList(List<FreeBean> freeList) {
		this.freeList = freeList;
	}

	public void readAndPopulateMalloc(String mallocFileName) {
		
		int errorsCount = 0;
		
		try (BufferedReader br = new BufferedReader( new java.io.FileReader( mallocFileName ))) {
			 
			String sCurrentLine;
 
			while ((sCurrentLine = br.readLine()) != null) {
				try {
					mallocList.add(parseMallocBean(sCurrentLine, new MallocBean()));
				} catch (Exception e) { 
					errorsCount++;
				} 
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		}  
		
		printErrors(mallocFileName, errorsCount);
	}

	public void readAndPopulateFree(String freeFileName) {
		
		int errorsCount = 0;
		
		try (BufferedReader br = new BufferedReader( new java.io.FileReader( freeFileName ))) {
			 
			String sCurrentLine;
 
			while ((sCurrentLine = br.readLine()) != null) {
				
				try {
					freeList.add(parseFreeBean(sCurrentLine, new FreeBean()));
				} catch (Exception e) { 
					errorsCount++;
				} 
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		printErrors(freeFileName, errorsCount);
	}
	
	
	

}
