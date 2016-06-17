public class TestTe {
	public static void main(String[] args) {

		// String
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < 50000; i++) {
			String result = "ANDHRA" ;
			result+= "TELANGANA ";
			result+= "RAYALASEEMA";
			result+=  "KOSTA" ;
			result+= "God" ;
			result+= "is" ;
			result+= "The truth" ;
			result+= "BJP" ;
			result+= "PRP";
			result+= "TRS" ;
			result+= "TDP" ;
			result+= "Cong";
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Time taken for string concatenation : " + (endTime - startTime) + " milli seconds");

		// String Buffer
		long startTime1 = System.currentTimeMillis();
		for (int i = 0; i < 50000; i++) {
			StringBuffer result = new StringBuffer();
			result.append("ANDHRA");
			result.append("TELANGANA");
			result.append("RAYALASEEMA");
			result.append("KOSTA");
			result.append("God");
			result.append("is");
			result.append("The truth");
			result.append("BJP");
			result.append("PRP");
			result.append("TRS");
			result.append("TDP");
			result.append("Cong");
		}
		long endTime1 = System.currentTimeMillis();
		System.out.println("Time taken for StringBuffer : " + (endTime1 - startTime1) + " milli seconds");
	}

}
