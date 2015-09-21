package diego.ufu.workloadoutput.main;

/**
 * Class that stores the info of a whole Experiment
 * @author Diego Costa
 *
 */
public class Experiment {
	
	String mallocFileName;
	String freeFileName;
	String path;
	String outputFile;
	
	public Experiment(String mallocFileName, String freeFileName,
			String path, String outputFile) {
		super();
		this.mallocFileName = mallocFileName;
		this.freeFileName = freeFileName;
		this.path = path;
		this.outputFile = outputFile;
	}

	public String getMallocFileName() {
		return mallocFileName;
	}

	public void setMallocFileName(String mallocFileName) {
		this.mallocFileName = mallocFileName;
	}

	public String getFreeFileName() {
		return freeFileName;
	}

	public void setFreeFileName(String freeFileName) {
		this.freeFileName = freeFileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}
	

}
