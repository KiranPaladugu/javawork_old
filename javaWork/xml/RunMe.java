import java.io.File;


public class RunMe {
	public static void main(String[] args) {
		XMLParser parser = new XMLParser(new File("myxml.xml"));
		parser.parse();
	}
}
