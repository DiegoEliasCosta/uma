package diego.ufu.workloadoutput.main;


public interface FileReader {

	MallocInfo loadFile(String mallocFileName, String freeFileName) throws Exception;
	
	


}
