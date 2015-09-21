package diego.ufu.workloadoutput.bean;

import java.util.Comparator;

public class TimeComparator implements Comparator<AllocBean>{

	@Override
	public int compare(AllocBean o1, AllocBean o2) {
		
		if(o1.getTime() > o2.getTime()) return 1;
		if(o1.getTime() == o2.getTime()) return 0;
		return -1;
	}

}
