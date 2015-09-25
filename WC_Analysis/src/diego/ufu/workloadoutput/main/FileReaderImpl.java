package diego.ufu.workloadoutput.main;

/**
 * Class responsible to create the analysis by the input
 * 
 * @author Diego
 *
 */
public class FileReaderImpl implements FileReader {
	
	/**
	 * Load Files from the disk.
	 * @return 
	 * @throws Exception 
	 * 
	 */
	public MallocInfo loadFile(String mallocFileName, String freeFileName) throws Exception {
		
		MallocInfo mallocInfo = new MallocInfo();
		
		// Malloc
		mallocInfo.readAndPopulateMalloc(mallocFileName);
		
		// Free
		mallocInfo.readAndPopulateFree(freeFileName);
		
		return mallocInfo;
				
	}

}
