package diego.ufu.workloadoutput.main;
import java.io.IOException;

import diego.ufu.workloadoutput.calculus.CalculusModule;


public interface StatisticAnalysis {
	
	public StringBuilder generateReport(MallocInfo info) throws IOException;

	public void addCalculusModule(CalculusModule object);

	public String printHeader();

}
