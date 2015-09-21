package diego.ufu.workloadoutput.bean;

/**
 * Enum for malloc operation.
 * 
 * @author Diego
 *
 */
public enum MallocOpEnum {
	
	MALLOC(1),
	
	CALLOC(2),
	
	REALLOC(3);
	
	private int code;
	
	private MallocOpEnum(int code){
		this.code = code;
	}
	
	public static MallocOpEnum parse(String code){
		return parse(Integer.parseInt(code));
	}
	
	public static MallocOpEnum parse(int code){
		if(code == MALLOC.code){
			return MallocOpEnum.MALLOC;
		} 
		
		if(code == REALLOC.code){
			return MallocOpEnum.REALLOC;
		} 
		
		if(code == CALLOC.code){
			return MallocOpEnum.CALLOC;
		} 
		
		return null;
	}
	
	
}
