import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
public class MyHandler extends DefaultHandler {
	public void startDocument (){
		System.out.println("Processing XML document Begin..");
		System.out.println("<Xml document version=\"1.0\">");
	}
	public void startElement (String uri, String localName,
		      String qName, Attributes attributes){
//		System.out.println("URI :"+uri);
//		System.out.println("LocalName:"+localName);
		System.out.print("<"+qName);
//		System.out.println("Attributes:");
		for(int i=0;i<attributes.getLength();i++)
			System.out.print(" "+attributes.getQName(i)+"="+attributes.getValue(i));
		System.out.print(">");
	}
	public void endElement(String uri, String localName, String qName){
//		System.out.println();
//		System.out.println("URI :"+uri);
//		System.out.println("LocalName:"+localName);
		System.out.println("<"+qName+"/>");
	}
}
