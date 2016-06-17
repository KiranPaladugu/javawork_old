import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;


public class WhoAmI {
	

	public static void main(String[] args) {
		Map<String, String> map;
		System.out.println("Configured Properties for Your System..");
		Properties properties = System.getProperties();
		Set<Entry<Object, Object>> set = properties.entrySet();
		Iterator<Entry<Object, Object>> itr = set.iterator();	
		while(itr.hasNext()){
			System.out.println(itr.next());
		}		
		System.out.println("\n\n\n\nHello...."+System.getProperty("user.name"));
		System.out.println("Your system Enviroment properties are.");
		map = System.getenv();
		Set<Entry<String, String>> set1 = map.entrySet();
		Iterator<Entry<String, String>> itr1 = set1.iterator();
		while(itr1.hasNext()){
			System.out.println(itr1.next());
		}
		
	}
}
