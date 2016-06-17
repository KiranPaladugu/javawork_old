package un.al;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class NeDetailPrinter {
	public NeDetailPrinter() {
	}
	
	private class NeDetails implements NeDetail{

		private String neName;
		private String neType;
		private String aInfo;
		
		public String getNeName() {
			return neName;
		}

		public String getNeType() {
			return neType;
		}

		public String getAdditionalInfo() {
			return aInfo;
		}
		
		protected void parse(String nename){
			nename.trim();
			String str[] = nename.split(",");
			if(str.length == 3){
				neName = str[0];
				neType = str[2];
				aInfo = str[1];
			}
			System.out.println("NeName="+neName +"  NeType="+neType+"   AdditionalInfo="+aInfo);
		}
		
	}
	
	public NeDetail getNeDetails(String msg){
		NeDetails ne = new NeDetails();
		ne.parse(printInLine(msg));
		return ne;
	}
	
	public String startParsing(File file){
		String str="";
		try {
			if(file.exists()){
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String msg = null;
			while((msg=reader.readLine()) !=null){
				str+="\n"+printInLine(msg);
			}
			}else{
				System.err.println("File Not found !!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public String printInLine(String msg){
		String newMsg = "";
		msg = msg.trim();
		if(msg.length()<2){
			System.err.println("cannot get details with : "+msg);
			return "";
		}
		if(msg.startsWith(">")){
			msg = msg.substring(1);
		}
		
		char ch[] = msg.toCharArray();
		int start = -1;
		int end = -1;
		int gtOccurence =0;
		int ltOccurence =0;
		for(int i =0;i<ch.length;i++){
			if(ch[i] == '<'){
				ltOccurence++;
				start = i+1;				
			}else if(ch[i] == '>'){
				gtOccurence++;
				end = i;
			}
			if(gtOccurence==1 && ltOccurence ==1){
				newMsg = msg.substring(start, end);
				break;
			}
		}
		if(gtOccurence==1 && ltOccurence ==1){
			newMsg = msg.substring(start, end);
		}else{
			System.err.println("Found Multiple <> occurences/No occurence for .... :-( :"+msg);
		}
		System.out.println(newMsg);
		return newMsg;
	}
	public static void main(String[] args){
		NeDetailPrinter printer = new NeDetailPrinter();
		File file = new File("C:\\Documents and Settings\\tcskirp\\Desktop\\new_2.txt");
		printer.startParsing(file);
	}
}
