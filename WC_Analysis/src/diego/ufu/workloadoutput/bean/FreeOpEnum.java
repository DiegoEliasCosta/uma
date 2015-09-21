package diego.ufu.workloadoutput.bean;

/**
 * Enum for free operation.
 * 
 * @author Diego
 *
 */
public enum FreeOpEnum {
	
	FREE(1);
	
	private int code;
	
	private FreeOpEnum(int code){
		this.code = code;
	}
	
	public static FreeOpEnum parse(int code){
		if(code == FREE.code){
			return FreeOpEnum.FREE;
		} 
		
		return null;
	}

	public static FreeOpEnum parse(String string) {
		return parse(Integer.parseInt(string));
	}

}
