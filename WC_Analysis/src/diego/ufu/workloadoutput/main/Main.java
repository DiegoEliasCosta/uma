package diego.ufu.workloadoutput.main;
import java.util.ArrayList;
import java.util.List;

import diego.ufu.workloadoutput.calculus.FrequencyOperationCalculus;
import diego.ufu.workloadoutput.calculus.QuantitiesCalculus;
import diego.ufu.workloadoutput.calculus.SizeFrequencyCalculus;
import diego.ufu.workloadoutput.calculus.SmartTimeRetentionCalculus;
import diego.ufu.workloadoutput.calculus.TotalTimeCalculus;


/**
 * Main class of the program.
 * 
 * @author Diego Costa
 *
 */
public class Main {

	/**
	 * @Parameter
	 * Index of the replications analyzes
	 * 	[REPLICATION_BEGIN, REPLICATION_END]
	 * 
	 */
	private static final int REPLICATION_BEGIN = 2;
	private static final int REPLICATION_END = 30;
	
	/** 
	 * Input File Format
	 * Current format = path + fileName + _ + replication + .csv 
	 *					(eg. path = "C:\" | filename = "MySQL_malloc" | 3 Replications
	 *					C:\MySQL_malloc_1.csv, C:\MySQL_malloc_2.csv, C:\MySQL_malloc_3.csv)
	 *
	 * See Main.generateFileName method to check how this is used
	 */
	private static String inputFileNameFormat = "%s" + "%s" + "_" + "%s" + ".csv";
	
	static FileReader reader;
	static StatisticAnalysis analysis;
	static List<Experiment> experiments = new ArrayList<>();
	
	private static String pathFolder;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
			
		 populateExperiments();
		 
		 analysis = new StatisticsAnalysisImpl();
		 addCalculusModule();
		 
		 for(Experiment exp : experiments) {
			 
			 System.out.println("[INFO] Starting the Workload Output Program");
			 System.out.println("[INFO] Parameters");
			 System.out.println("[INFO] Start Replications:" + REPLICATION_BEGIN);
			 System.out.println("[INFO] End Replications: " + REPLICATION_END);
			 System.out.println("[INFO] Input Path Folder: " + exp.getPath());
			 System.out.println("[INFO] Output Path File: " + exp.getOutputFile());
			 System.out.println("[INFO] Malloc File Pattern: " + exp.getMallocFileName());
			 System.out.println("[INFO] Free File Pattern: " + exp.getFreeFileName());
			 
			 try {
				 // Measuring the time spent in the analysis
				 long startTime = System.currentTimeMillis();
				 // Execute the analysis
				 executeAnalysis(exp);
				 long endTime = System.currentTimeMillis();
				 
				 System.out.println("[INFO] Time elapsed (seconds): " 
						 			+ ((endTime - startTime) / 1000));
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
		 
		 }
			
	}

	private static void executeAnalysis(Experiment exp) throws Exception {
		
		 reader = new FileReaderImpl();
		 pathFolder = exp.getPath();

		 StringBuilder programReport = new StringBuilder();
		 programReport.append(analysis.printHeader());
		 
		 System.out.println("[INFO] Reading: ");
		 System.out.print("     ");
		 
		 // Get all the replications
		 for(int i = REPLICATION_BEGIN; i <= REPLICATION_END; i++) {
			 System.out.print(" | " + i + " | ");
			 
			 // Generate the malloc/free file name pattern
			 String mallocFileName = generateFileName(exp.getMallocFileName(), i);
			 String freeFileName = generateFileName(exp.getFreeFileName(), i);
			 
			 // Read both malloc/free file
			 MallocInfo info = reader.loadFile(mallocFileName, 
					 freeFileName);
			 
			 // Generate the Report
			 StringBuilder report = analysis.generateReport(info);
			 
			 // Add to the report 
			 programReport.append(report);
		 }
		 
		 System.out.println("[INFO] Writing the output file...");
		 // Write the into in the output file
		 Utils.writeFile(exp.getOutputFile(), programReport);
		
	}
	
	/**
	 * Generate the FileName of the malloc/free operations based on the
	 * inputNameFormat variable
	 * 
	 */
	private static String generateFileName(String fileName, int replicationID) {
		return String.format(inputFileNameFormat, pathFolder, fileName, replicationID);
	}


	/**
	 * Here I add the INFO about each experiment that should be analyzed
	 */
	private static void populateExperiments() {
		
		/**
		 * @Parameter
		 * Here you should add the experiment that should be analyzed with the 
		 * four specified parameters (path, mallocFile, freeFile, outputFilePath)
		 * 
		 */
		// Example: MySQL
		experiments.add(new ExperimentBuilder()
				.withPath("D:/UFU/WC - MySQL/")
				.withMallocFileName("mySQL_malloc")
				.withFreeFileName("mySQL_free")
				.withOutputFilePath("D:/UFU/WC - Result/MySQL_Result.csv")
				.build());
	}

	/**
	 * In this method we decide WHICH analyze should be performed
	 */
	private static void addCalculusModule() {
		
			/**
			 * @Parameter
			 * Retrieves the total time of the experiment
			 * Total Time Experiment = time from the first to the last 
			 * dynamic memory operation (alloc/dealloc)
			 */
			analysis.addCalculusModule(new TotalTimeCalculus());
		
		
			/**
			 * @Parameter
			 * Analyzes the quantities of allocations/deallocations
			 *  # of Allocations
			 *	# of Deallocations                    
			 *	# of NULL Deallocations                
			 *	% non-deallocated allocations  -> (alloc - dealloc)/ alloc       
			 *	Total of Requested Memory  
			 *  Average Size of Allocation    
			 */
			analysis.addCalculusModule(new QuantitiesCalculus());
			
			/**
			 * @Parameter
			 * Analyzes the frequency of the allocation operation (malloc, realloc, cealloc)
			 * 	# malloc() 
			 *	# calloc() 
			 *	# realloc()
			 *	% malloc() 
			 *	% calloc() 
			 *	% realloc()
			 */
			analysis.addCalculusModule(new FrequencyOperationCalculus());
			
			/**
			 * @Parameter
			 * Analyzes the frequency of the allocated sizes by Size Category (SC)
			 * 	Frequency by SC (see SizeCategory class)
			 *  Distinct allocated sizes by SC (see SizeCategory class)
			 *  10 Most allocated sizes
			 *  Number of distinct size allocations (total)
			 */
			analysis.addCalculusModule(new SizeFrequencyCalculus());
		
			/**
			 * @Parameter
			 * Analyzes the Retention Time (RT) of the allocations
			 * For each TimeRetentionCategory (see TimeRetentionCategory class) it provides:
			 *  Average RT
             *  RT Variance 
             *  RT Standard Deviation
             *  Minimum RT
             *  Maximum RT
			 */
			analysis.addCalculusModule(new SmartTimeRetentionCalculus());
			
			/**
			 * @Parameter
			 * This is a temporal analyzes between allocation/deallocation
			 * This should not be used with the other analyzes as it brings 
			 * a huge amount of data (basically all alloc and dealloc sorted 
			 * by time)
			 */
			//analysis.addCalculusModule(new AllocDeallocBehaviorCalculus());
			
			/**
			 * @Parameter
			 * Analyzes the sizes frequency and returns a map containing
			 * 	[allocation size = # of allocations]
			 * This should not be used with the other analyzes and should be executed 
			 * per replication as it brings two columns with [size] [# of allocations]
			 *  
			 *	  
			 */
			//analysis.addCalculusModule(new AllSizeFrequencyCalculus());
		
	}

}
