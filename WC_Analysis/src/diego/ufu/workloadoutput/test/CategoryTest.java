package diego.ufu.workloadoutput.test;

import diego.ufu.workloadoutput.bean.SizeCategory;

public class CategoryTest {

	public static void main(String[] args) {
		
		for(int i = 1; i < 1026; i ++) {
			System.out.println(i + "_" + SizeCategory.findCategory(i));
		}
		
	}
	
}
