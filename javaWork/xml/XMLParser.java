import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.HandlerBase;
import org.xml.sax.SAXException;

public class XMLParser{
	private File file;
	private InputStream inputStream;	
	public XMLParser(File file){
		this.file=file;
		System.out.println("Started Parseing File:"+file.getAbsolutePath());
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		}
	}
	public void parse(){
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(file, new MyHandler());
			
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {		
			e1.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}		
		
	}
}
