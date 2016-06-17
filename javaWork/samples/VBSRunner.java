import java.io.IOException;


public class VBSRunner {
 public static void main(String[] args) {
	Runtime run = Runtime.getRuntime();
	try {
		 run.exec("cscript  fun.vbs");		
	} catch (IOException e) {		
		e.printStackTrace();
	}
}
}
