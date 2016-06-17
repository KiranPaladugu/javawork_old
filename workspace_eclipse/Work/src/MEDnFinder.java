import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.events.EndDocument;

public class MEDnFinder {
	private Date startDate,endDate;
	private long start;
	private long end;
	private File source;
	private String name = "> Creating ME <";
	private String comName = "> Couldn't find any ME with MEDn <";
	private Set<String> set = new HashSet<String>();
	private Set<String> compare = new HashSet<String>();

	public MEDnFinder(File source) {
		super();
		startDate = new Date();
		start = startDate.getTime();
		this.source = source;
		process();
	}

	private void process() {
		if (source != null) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(source));
				String line = null;
				while ((line = br.readLine()) != null) {
					validateLine(line);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void validateLine(String line) {
		if (line.contains(name)) {
			int ind = line.indexOf(name);
			if (ind != -1) {
				String str = line.substring(name.length(), line.length());
				str = str.trim();
				str = str.replace(">", "").trim();
				set.add(str);
			}
		}
	}

	public void doAdd(String string) {
		validateLine(string);
	}

	public void compareAll(String[] strings) {
		for (String string : strings) {
			compare(string);
		}
	}

	public void compare(File file) {
		if (file != null) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = null;
				while ((line = br.readLine()) != null) {
					validateCompare(line);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		compare(true);
		endDate = new Date();
		end = endDate.getTime();
		System.out.println("Done in : "+(end - start)+" millis...");
	}

	private void compare(boolean b) {
		File file = new File("C:\\Users\\ekirpal\\Documents\\compare.log");
		if (!file.exists()) {
			new File(file.getParent()).mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for (String str : compare) {
				String msg = compare(str);
				if (msg.length() > 0)
					bw.write(compare(str) + "\n");
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void validateCompare(String line) {
		if (line.contains(comName)) {
			int ind = line.indexOf(comName);
			if (ind != -1) {
				String str = line.substring(comName.length(), line.length());
				str = str.trim();
				str = str.replace(">", "").trim();
				compare.add(str);
			}
		}

	}

	public String compare(String string) {
		String msg = "";
		if (set.contains(string)) {
			msg = string + " \t\t < O.K! > !!";
		} else {
			msg = ">> "+string + " is NOT FOUND!! ****";
		}
		System.out.println(msg);
		return msg;
	}

	public void dump() {
		StringBuilder str = new StringBuilder("");
		for (String string : set) {
			str.append(string + "\n");
		}
		System.out.println(str.toString());
	}

	public static void main(String args[]) {
		File filename = new File("C:\\Users\\ekirpal\\Documents\\LOG\\TMFAgent.log");
		MEDnFinder finder = new MEDnFinder(filename);
		// finder.doAdd(">     me : MEId STRING : 'bgehp027:3:10129'");
		finder.compare(new File("C:\\Users\\ekirpal\\Documents\\LOG\\TMFAgent.log"));		
//		finder.dump();
	}

}
