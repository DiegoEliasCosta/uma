package diego.ufu.workloadoutput.main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility Class.
 * 
 * @author Diego
 *
 */
public class Utils {

	/**
	 * List all files in a folder
	 * 
	 * @param folder
	 * @return
	 */
	public static List<String> listFilesInFolder(File folder) {
		
		List<String> filesList = new ArrayList<>(60);
		
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	filesList.addAll(listFilesInFolder(fileEntry));
	        } else {
	            filesList.add(fileEntry.getName());
	        }
	    }
		
		return filesList;
	}

	/**
	 * Read the file and returns each line as String
	 * 
	 * @param mallocFile
	 * @return
	 */
	public static List<String> readFile(String mallocFile) {
		
		List<String> file = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader( new java.io.FileReader( mallocFile ))) {
 
			String sCurrentLine;
 
			while ((sCurrentLine = br.readLine()) != null) {
				file.add(sCurrentLine);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return file;
		
	}

	public static void writeFile(String fileName, StringBuilder content) throws IOException {
		
	        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));  
	        out.append(content.toString());  
	        out.flush();  
	        out.close();  
		
	}

}
