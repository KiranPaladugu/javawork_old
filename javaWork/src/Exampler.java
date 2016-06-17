import java.net.URL;
import java.security.CodeSource;

public class Exampler {
	public static void main(String[] args) throws Exception {
		new Exampler();
	}

	public Exampler() throws Exception {
		CodeSource src = Exampler.class.getProtectionDomain().getCodeSource();
		if (src != null) {
			URL url = new URL(src.getLocation(), "MyApp.properties");
			System.out.println(url);
		} else {
			/* Fail... */
		}
	}
}
