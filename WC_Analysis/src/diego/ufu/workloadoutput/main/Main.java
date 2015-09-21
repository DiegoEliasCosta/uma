package diego.ufu.workloadoutput.main;
import java.util.ArrayList;
import java.util.List;

import diego.ufu.workloadoutput.calculus.AllSizeFrequencyCalculus;
import diego.ufu.workloadoutput.calculus.AllocDeallocBehaviorCalculus;
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

	private static final String POS_FIX = "Size Frequency";
	/*
	 * ------- Parameters ---------
	 * Please see populateExperiment and mainly addCalculus method
	 * FIXME: This should be done in a better way to increase the flexibility 
	 */
	private static final int REPLICATION_BEGIN = 2;
	private static final int REPLICATION_END = 2;
	
	private static boolean shouldCalculateTimeRetention = false;
	private static boolean shouldOnlyCalculateTimeRetention = false;
	
	private static boolean calculateOnlyAllocDeallocBehavior = false; // PRIORITY
	private static boolean totalTime = false;
	private static boolean shouldOnlyCalculateSizeFrequency = true;
	// ----------------------------
	// 
	private static boolean calculateForBigAlgorithms = false;
	
	static FileReader reader;
	static StatisticAnalysis analysis;
	
	static List<Experiment> experiments = new ArrayList<>();
	
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
					 executeExperiment(exp);
				 } catch (Exception e) {
					 e.printStackTrace();
				 }
			 
			 }
			
	}

	private static void executeExperiment(Experiment exp) throws Exception {
		
		
		 long startTime = System.currentTimeMillis();
		
		 reader = new FileReaderImpl(exp.getPath());

		 StringBuffer stringbuffer = new StringBuffer();
		 stringbuffer.append(analysis.printHeader());
		 
		 System.out.println("[INFO] Reading: ");
		 System.out.print("     ");
		 
		 // Get all the replications
		 for(int i = REPLICATION_BEGIN; i <= REPLICATION_END; i++) {
			 System.out.print(" | " + i + " | ");
			 // Read the file
			 MallocInfo info = reader.loadFile(exp.getMallocFileName(), exp.getFreeFileName(), i);
			 // Generate the Report
			 StringBuffer report = analysis.generateReport(info);
			 stringbuffer.append(report);
		 }
		 
		 System.out.println("[INFO] Writing the output file...");
		 // Write the into in the output file
		 Utils.writeFile(exp.getOutputFile(), stringbuffer);
		 
		 long endTime = System.currentTimeMillis();
		 
		 System.out.println("[INFO] Time elapsed (seconds): " 
				 			+ ((endTime - startTime) / 1000));
		
	}

	/**
	 * Here I add the INFO about each experiment that should be analyzed
	 */
	private static void populateExperiments() {
		
		// MySQL
		experiments.add(new Experiment("mySQL_malloc", "mySQL_free", "D:/UFU/WC - MySQL/", "D:/UFU/WC - Result/MySQL " + POS_FIX +".csv"));
	
		// Lynx
		experiments.add(new Experiment("lynx_malloc", "lynx_free", "D:/UFU/WC - Lynx/", "D:/UFU/WC - Result/Lynx " + POS_FIX + ".csv"));
		
		// Octave Simple
		experiments.add(new Experiment("octave_malloc", "octave_free", "D:/UFU/WC - Octave/", "D:/UFU/WC - Result/Octave Simple " + POS_FIX + ".csv"));
		
		// Octave Complex
		experiments.add(new Experiment("octave_malloc", "octave_free", "D:/UFU/WC - Octave_Complex/", "D:/UFU/WC - Result/Octave Complex " + POS_FIX + ".csv"));
		
		// VLCPlayer Audio
		experiments.add(new Experiment("vlcplayer_malloc", "vlcplayer_free", "D:/UFU/WC - VLCPlayer/Audio/", "D:/UFU/WC - Result/VLCPlayer Audio " + POS_FIX + ".csv"));
		
		// VLCPlayer Video
		experiments.add(new Experiment("vlcplayer_malloc", "vlcplayer_free", "D:/UFU/WC - VLCPlayer/Video/", "D:/UFU/WC - Result/VLCPlayer Video " + POS_FIX + ".csv"));
		
		// Cherokee
		experiments.add(new Experiment("cherokee_malloc", "cherokee_free", "D:/UFU/WC - Cherokee/", "D:/UFU/WC - Result/Cherokee " + POS_FIX + ".csv"));
	
		if(calculateForBigAlgorithms) {
			// Inkscape
			experiments.add(new Experiment("inkscape_malloc", "inkscape_free", "D:/UFU/WC - Inkscape/", "D:/UFU/WC - Result/Inkscape " + POS_FIX + ".csv"));
			
			// CodeBlocks
			experiments.add(new Experiment("codeblock_malloc", "codeblock_free", "D:/UFU/WC - CodeBlocks/", "D:/UFU/WC - Result/CodeBlocks " + POS_FIX + ".csv"));
			
		}
		
	}

	/**
	 * In this method we decide WHICH analyze should be done
	 */
	private static void addCalculusModule() {
		
		if(totalTime) {
			analysis.addCalculusModule(new TotalTimeCalculus());
			return;
		}
		
		if(shouldOnlyCalculateSizeFrequency) {
			analysis.addCalculusModule(new AllSizeFrequencyCalculus());
			return;
		}
		
		if(calculateOnlyAllocDeallocBehavior) {
			analysis.addCalculusModule(new AllocDeallocBehaviorCalculus());
			//analysis.addCalculusModule(new AllocDeallocGroupCalculus());
			return; // FIXME
		}
		
		if(!shouldOnlyCalculateTimeRetention) {
			analysis.addCalculusModule(new QuantitiesCalculus());
			analysis.addCalculusModule(new FrequencyOperationCalculus());
			analysis.addCalculusModule(new SizeFrequencyCalculus());
			analysis.addCalculusModule(new TotalTimeCalculus());
		}
		
		if(shouldCalculateTimeRetention || shouldOnlyCalculateTimeRetention) {
			//analysis.addCalculusModule(new TimeRetentionCalculus());
			analysis.addCalculusModule(new SmartTimeRetentionCalculus());
		}
		
		
	}

}
