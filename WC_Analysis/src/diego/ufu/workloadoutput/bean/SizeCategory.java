package diego.ufu.workloadoutput.bean;


public enum SizeCategory {
	
	C1(0, 16),
	C2(17, 64),
	C3(65, 256),
	C4(257, 512),
	C5(513, 1024),
	C6(1025, Long.MAX_VALUE);
	
	long inf;
	long sup;
	
	private SizeCategory(long inf, long sup) {
		this.inf = inf;
		this.sup = sup;
	}
	
	private static boolean isBetween(int v, SizeCategory sc) {
		return (sc.inf <= v && sc.sup >= v); 
	}
	
	public static SizeCategory findCategory(int value) {
		
		if(isBetween(value, C1)) return C1;
		if(isBetween(value, C2)) return C2;
		if(isBetween(value, C3)) return C3;
		if(isBetween(value, C4)) return C4;
		if(isBetween(value, C5)) return C5;
		if(isBetween(value, C6)) return C6;
		
		return null;
	}
	
	public String getCategoryValues() {
		return "(" + this.inf + "," + this.sup + ")"; 
	}
	

}
