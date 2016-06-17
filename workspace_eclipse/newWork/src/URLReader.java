import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class URLReader {
	private URL fURL;
	private static final String HTTP = "http";
	private static final String HEADER = "header";
	private static final String CONTENT = "content";
	//private static final String END_OF_INPUT = "\\Z";
	private static final String NEWLINE = System.getProperty("line.separator");

	public URLReader(String aUrlName) throws MalformedURLException {
		this(new URL(aUrlName));
	}

	public URLReader(URL aURL) {
		if (!HTTP.equals(aURL.getProtocol())) {
			throw new IllegalArgumentException("URL is not for HTTP Protocol: " + aURL);
		}
		fURL = aURL;
		System.out.println("URL is : " + fURL);
	}

	public String getPageContent() {
		String result = null;
		URLConnection connection = null;
		try {
			log("Opening Connection...");
			connection = fURL.openConnection();
			List<Proxy> proxies = java.net.ProxySelector.getDefault().select(fURL.toURI());
			System.out.println("Proxy size : "+proxies.size());
			if (proxies.size() > 0) {
				Proxy proxy = proxies.get(0);
				System.out.println("Proxy is :" + proxy);
				fURL.openConnection(proxy);
				log("Connection opening completed...");
			}
			log("Scaning .....");
			int x = -1;
			result = "";
			while ((x = connection.getInputStream().read()) != -1) {
				System.out.print((char) x);
				result += (char) x;
			}
			// Scanner scanner = new Scanner(connection.getInputStream());
			// log("Scaning COMPLETED !.....");
			// scanner.useDelimiter(END_OF_INPUT);
			// result = scanner.next();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return result;
	}

	private static void log(Object aObject) {
		System.out.println(aObject);
	}

	public String getPageHeader() {
		StringBuilder result = new StringBuilder();

		URLConnection connection = null;
		try {
			connection = fURL.openConnection();
		} catch (IOException ex) {
			log("Cannot open connection to URL: " + fURL);
		}

		// not all headers come in key-value pairs - sometimes the key is
		// null or an empty String
		int headerIdx = 0;
		String headerKey = null;
		String headerValue = null;
		while ((headerValue = connection.getHeaderField(headerIdx)) != null) {
			headerKey = connection.getHeaderFieldKey(headerIdx);
			if (headerKey != null && headerKey.length() > 0) {
				result.append(headerKey);
				result.append(" : ");
			}
			result.append(headerValue);
			result.append(NEWLINE);
			headerIdx++;
		}
		return result.toString();
	}

	public static void main(String... aArgs) throws MalformedURLException {
		String url = "www.amt.genova.it";
		String option = "content";
		URLReader fetcher = new URLReader(url);
		if (HEADER.equalsIgnoreCase(option)) {
			log(fetcher.getPageHeader());
		} else if (CONTENT.equalsIgnoreCase(option)) {
			log(fetcher.getPageContent());
		} else {
			log("Unknown option.");
		}
	}

}
