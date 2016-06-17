import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class Tail {
	public static void main(String args[]) {
		int num = 50;
		try {
			if (args.length == 1) {
				String fileName = (String) args[0];
				if ((fileName == null) || (fileName.equals(""))) {
					System.out.println("- No File Specified.\n Possible Usage ..");
					System.exit(0);
				} else
					fileName.trim();
				File f = new File(fileName);
				new Tail(f);
			} else if (args.length == 2) {
				try {
					num = Integer.parseInt(args[0]);
				} catch (Exception e) {
					System.out.println("Not a valid number... \n Possible Usage..");
				}
				String fileName = (String) args[1];
				if ((fileName == null) || (fileName.equals(""))) {
					System.out.println("- No File Specified.\n Possible Usage ..\nNot valid file or no file found.");
					System.exit(0);
				} else
					fileName.trim();
				File f = new File(fileName);
				new Tail(f, num);
			} else {
				throw new Exception("NOT VALID PARAMETERS..");
			}
		} catch (Exception e) {
			System.out.println("- No File Specified.\n Possible Usage ..");
			e.printStackTrace();
		}
	}
	FileInputStream fis;
	File file;
	FileReader fr;
	BufferedReader br;

	long lines = 0;

	Tail() {
	}

	Tail(File file) {
		this.file = file;
		this.tailRead(file);
	}

	Tail(File file, int number) {
		this.file = file;
		this.getLines(file);
		this.tailLinePosition(file, number);
	}

	public long getLines(File file) {
		FileReader fr = null;
		LineNumberReader lineReader;
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			System.out.println("****** FILE NOT FOUND ***********");
			System.exit(0);
		}
		lineReader = new LineNumberReader(fr);						
			lines = lineReader.getLineNumber();
			// System.out.println("Total lines:"+lineReader.getLineNumber());		
		return lines;
	}

	public void tailLinePosition(File file, int number) {
		FileReader fr = null;
		LineNumberReader lineReader;
		long atPostion = lines - number;
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			System.out.println("****** FILE NOT FOUND ***********");
			System.exit(0);
		}
		lineReader = new LineNumberReader(fr);
		try {
			while (true) {
				String msg = lineReader.readLine();
				if (msg == null) {
					Thread.sleep(1000);
				} else {
					if (lineReader.getLineNumber() >= atPostion) {
						System.out.println(msg);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void tailRead(File file) {
		this.file = file;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			while (true) {
				String msg = br.readLine();
				if (msg == null) {
					Thread.sleep(1000);
				} else
					System.out.println(msg);
			}
		} catch (FileNotFoundException e) {
			System.out.println(file.getName() + " * * * File Not Found * * *");
		} catch (IOException e) {
			System.out.println(file.getName() + " * * * Unable to read * * *");
		} catch (InterruptedException e) {
			System.out.println("* * * Unable to handle * * *");
		}
	}
}
