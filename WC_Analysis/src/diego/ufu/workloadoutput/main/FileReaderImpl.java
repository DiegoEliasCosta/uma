package diego.ufu.workloadoutput.main;
import java.util.List;


/**
 * Class responsible to create the analysis by the input
 * 
 * @author Diego
 *
 */
public class FileReaderImpl implements FileReader {
	
	String pathFolder;
	
	public FileReaderImpl(String pathFolder) {
		
		this.pathFolder = pathFolder;
	}

	/**
	 * Load Files from the disk.
	 * @return 
	 * @throws Exception 
	 * 
	 */
	public MallocInfo loadFile(String mallocFilePattern, String freeFilePattern, 
			int replicationNumber) throws Exception {
		
		MallocInfo mallocInfo = new MallocInfo();
		
		// Malloc
		String mallocFileName = generateFileName(mallocFilePattern, replicationNumber);
		mallocInfo.readAndPopulateMalloc(mallocFileName);
		
		// Free
		String freeFileName = generateFileName(freeFilePattern, replicationNumber);
		mallocInfo.readAndPopulateFree(freeFileName);
		
		return mallocInfo;
				
	}
		

	private String generateFileName(String mallocFileName, int i) {
		return pathFolder + mallocFileName + "_" + i + ".csv";
	}

}
