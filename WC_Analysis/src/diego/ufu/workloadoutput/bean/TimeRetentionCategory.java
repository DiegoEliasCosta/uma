package diego.ufu.workloadoutput.bean;


public enum TimeRetentionCategory {
	
	TR_C1(0, 0.1),
	TR_C2(0.1, 0.2),
	TR_C3(0.2, 0.3),
	TR_C4(0.3, 0.4),
	TR_C5(0.4, 0.5),
	TR_C6(0.5, 0.6),
	TR_C7(0.6, 0.7),
	TR_C8(0.7, 0.8),
	TR_C9(0.8, 0.9),
	TR_C10(0.9, 1.0),
	TR_C11(1.0, 2.0),
	TR_C12(2.0, Double.MAX_VALUE),
	TR_NULL(0,0);
	
	private static final int BILLION = 1000000000;
	double inf;
	double sup;
	
	private TimeRetentionCategory(double inf, double sup) {
		this.inf = inf;
		this.sup = sup;
	}
	
	private static boolean isBetween(long v, TimeRetentionCategory sc) {
		return ((sc.inf * BILLION <= v) && (sc.sup * BILLION > v)); 
	}
	
	public static TimeRetentionCategory findCategory(long timeRetention) {
		
		if(isBetween(timeRetention, TR_C1)) return TR_C1;
		if(isBetween(timeRetention, TR_C2)) return TR_C2;
		if(isBetween(timeRetention, TR_C3)) return TR_C3;
		if(isBetween(timeRetention, TR_C4)) return TR_C4;
		if(isBetween(timeRetention, TR_C5)) return TR_C5;
		if(isBetween(timeRetention, TR_C6)) return TR_C6;
		if(isBetween(timeRetention, TR_C7)) return TR_C7;
		if(isBetween(timeRetention, TR_C8)) return TR_C8;
		if(isBetween(timeRetention, TR_C9)) return TR_C9;
		if(isBetween(timeRetention, TR_C10)) return TR_C10;
		if(isBetween(timeRetention, TR_C11)) return TR_C11;
		if(isBetween(timeRetention, TR_C12)) return TR_C12;
		
		return TR_NULL;
	}
	
	public String getCategoryValues() {
		return "(" + this.inf + "," + this.sup + ")"; 
	}

	public double getSup() {
		return sup;
	}
	

}
