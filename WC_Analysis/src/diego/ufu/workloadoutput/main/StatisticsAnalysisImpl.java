package diego.ufu.workloadoutput.main;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import diego.ufu.workloadoutput.calculus.CalculusModule;

/**
 * Implements the utilities for controlling the experiment analysis
 * 
 * @author Diego Costa
 *
 */
public class StatisticsAnalysisImpl implements StatisticAnalysis {
	
	private static final String BREAK_LINE = "\n";
	List<CalculusModule> calculusSequence = new ArrayList<>();
	
	@Override
	public void addCalculusModule(CalculusModule object) {
		calculusSequence.add(object);
		
	}


	@Override
	public StringBuffer generateReport(MallocInfo mallocByRep) throws IOException {
		
		StringBuffer stringbuffer = new StringBuffer();
		
		for(CalculusModule calculus : calculusSequence) {
			stringbuffer.append(calculus.calculate(mallocByRep));
		}
		stringbuffer.append(BREAK_LINE);
		
		return stringbuffer;
		
	}


	@Override
	public String printHeader() {
		
		StringBuffer buffer = new StringBuffer();
		
		for(CalculusModule calculus : calculusSequence) {
			buffer.append(calculus.getHeader());
		}
		
		buffer.append(BREAK_LINE);
		
		return buffer.toString();
	}

}
