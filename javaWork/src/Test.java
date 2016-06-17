import java.util.Date;

public class Test {
	public static void main(String args[]) {
		Date date = new Date();
		date.setHours(10);
		System.out.println("Modified date :" + date);
		System.out.println(date.toLocaleString());
		System.out.println(date.getTime());
		Date dt = new Date();
		System.out.println("Modified date :" + dt);
		System.out.println(dt.toLocaleString());
		System.out.println(dt.getTime());
	}
}
