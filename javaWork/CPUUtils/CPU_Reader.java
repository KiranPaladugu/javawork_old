import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CPU_Reader {
	public static final int SAVE_TO_TEXT_FILE = 1;
	public static final int SAVE_TO_EXCEL_FILE = 2;
	public static final int SAVE_TO_TEXT_AND_EXCEL_FILES = 3;

	private int saveMode = SAVE_TO_TEXT_AND_EXCEL_FILES;;
	private File file;
	private FileReader reader;
	private BufferedReader buffer;
	private String first = "";
	private int count;
	private FileWriter printer;
	private boolean isSkip, append;
	private BufferedWriter writer;
	private String newLine = "\n";
	private String savePath = "";
	private int nCol;
	private boolean linux, isOne;
	private Vector<String> process = new Vector<String>();
	private Vector<Vector<Vector<String>>> whole = new Vector<Vector<Vector<String>>>();
	private StringBuffer memText = new StringBuffer("");

	public CPU_Reader(File file) {
		this.file = file;
	}

	private void initReader() {
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "File not Found !!", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		buffer = new BufferedReader(reader);
	}

	public void setSaveMode(int saveMode) {
		this.saveMode = saveMode;
	}

	public void setAppend(boolean flag) {
		this.append = flag;
	}

	public CPU_Reader(File file, String savePath) {
		this.file = file;
		this.savePath = savePath;
		initReader();
	}

	public CPU_Reader(File file, String savePath, boolean isSkip) {
		this.file = file;
		this.savePath = savePath;
		this.isSkip = isSkip;
		initReader();
	}

	public File getFile() {
		return file;
	}

	public String startRead() {
		boolean isMem = veryInput();
		String msg = "";
		try {
			while ((msg = buffer.readLine()) != null) {
				if (isMem) {
					parseFile(msg);
				} else
					parseLine(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "IO Exception ..!!", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		System.out.println("TOTAL ROWS PROCESSED :" + count);
		if (isMem) {
			msg = saveMemText();
		} else {
			msg = determineSave();
		}
		return msg;
	}

	private String saveMemText() {
		System.out.println("Now Writing..");
		String msg = "";
		try {
			File newFile = new File(savePath + file.getName() + ".txt");
			FileOutputStream fos = new FileOutputStream(newFile);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			byte b[] = memText.toString().getBytes();
			bos.write(b);
			bos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			msg += "\nWrite Failed. Because\n" + e.getLocalizedMessage();
		} catch (Exception e) {
			e.printStackTrace();
			msg += "\nWrite Failed. Because\n" + e.getLocalizedMessage();
		}
		msg += file.getName() + " can only saved into text file.";
		System.out.println("Done write Operation..");
		return msg;
	}

	private void parseFile(String msg) {
		// method to read .mem file.
		count++;
		memText.append(getMemText(msg));
	}

	private String getMemText(String msg) {
		if (msg.startsWith("System:")) {
			return parseSystem(msg);
		} else if (msg.startsWith("Memory:")) {
			return parseMemory(msg);
		} else {
			return new String("");
		}
	}

	private String parseMemory(String msg) {
		StringBuffer str = new StringBuffer("");
		Vector<String> vector = splitLine(msg, ",");
		for (int i = 0; i < vector.size(); i++) {
			if (i == 0) {
				Vector<String> v = splitLine(vector.get(i), " ");
				for (int ii = 0; ii < v.size(); ii++) {
					if (ii == 0)
						continue;
					str.append(v.get(ii) + " ");
				}
				str.append("\t");
			} else if (i == 1) {
				Vector<String> v = splitLine(vector.get(i), " ");
				for (int ii = 0; ii < v.size(); ii++) {
					str.append(v.get(ii) + " ");
				}
				str.append("\t");
			} else if (i == 2) {
				Vector<String> v = splitLine(vector.get(i), " ");
				for (int ii = 0; ii < v.size(); ii++) {
					if (ii > 1)
						break;
					str.append(v.get(ii) + " ");
				}
				str.append("\n");
			}
		}
		return str.toString();
	}

	private String parseSystem(String msg) {
		StringBuffer str = new StringBuffer("");
		Vector<String> vector = splitLine(msg, " ");
		for (int i = 0; i < vector.size(); i++) {
			if (i < 1) {
				continue;
			} else if (i < 6) {
				str.append(vector.get(i) + "_");
			} else {
				str.append(vector.get(i) + ":\t");
			}
		}
		return str.toString();
	}

	private boolean veryInput() {
		boolean flag = false;
		if (file.getName().endsWith(".mem"))
			flag = true;
		return flag;
	}

	private String determineSave() {
		String msg = "";
		if (saveMode == SAVE_TO_EXCEL_FILE) {
			msg += writeToExcel();
		} else if (saveMode == SAVE_TO_TEXT_FILE) {
			msg += writeToTxtFile();
		} else if (saveMode == SAVE_TO_TEXT_AND_EXCEL_FILES) {
			msg += writeToTxtFile();
			msg += writeToExcel();
		}
		return msg;
	}

	private String writeToTxtFile() {
		int i = 0;
		String msg = "";
		for (Vector<Vector<String>> v : whole) {
			try {
				String name = process.get(i).trim();
				if (name.contains(":")) {
					name = name.replaceAll(":", "");
				}
				if (append) {
					printer = new FileWriter(new File(savePath + name + ".txt"), true);
				} else {
					printer = new FileWriter(new File(savePath + file.getName() + "_" + name + ".txt"));
				}
				writer = new BufferedWriter(printer);
				for (Vector<String> vv : v) {
					writer.append(getTabToWrite(vv) + newLine);
					writer.flush();
				}
				writer.close();
				printer.close();
				writer = null;
				printer = null;
				i++;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				msg += "\nWrite Failed. Because\n" + e.getLocalizedMessage();
			} catch (IOException e) {
				e.printStackTrace();
				msg += "\nWrite Failed. Because\n" + e.getLocalizedMessage();
			} catch (Exception e) {
				e.printStackTrace();
				msg += "\nWrite Failed. Because\n" + e.getLocalizedMessage();
			}
		}
		msg += "\nWrite to Text File(s) is Success.";
		return msg;
	}

	private String writeToExcel() {
		String msg = "";
		File file = new File(savePath + this.file.getName() + ".xls");
		try {
			FileOutputStream fOut = new FileOutputStream(file, true);
			HSSFWorkbook workBook = new HSSFWorkbook();
			int sheetNum = 0;
			for (Vector<Vector<String>> v : whole) {
				String sheetName = process.get(sheetNum).trim();
				if (sheetName.contains(":")) {
					sheetName = sheetName.replaceAll(":", "");
				}
				HSSFSheet sheet = workBook.createSheet(sheetName);
				int rowNum = 0;
				for (Vector<String> vv : v) {
					HSSFRow row = sheet.createRow(rowNum);
					for (int col = 0; col < vv.size(); col++) {
						HSSFCell cell = row.createCell(col);
						cell.setAsActiveCell();
						try {
							cell.setCellValue(Double.parseDouble(vv.get(col)));
						} catch (NumberFormatException e) {
							cell.setCellValue(vv.get(col));
						}
					}
					rowNum++;
				}
				sheetNum++;
			}
			workBook.write(fOut);
			fOut.close();
			workBook.close();
			fOut = null;
		} catch (IOException i) {
			i.printStackTrace();
			msg += "\nWrite Failed. Because\n" + i.getLocalizedMessage();
		} catch (Exception e) {
			e.printStackTrace();
			msg += "\nWrite Failed. Because\n" + e.getLocalizedMessage();
		}
		msg += "\nWrite to Excel File(s) is Success.";
		return msg;
	}

	public String getTabToWrite(Vector<String> vector) {
		String str = "";
		for (String s : vector) {
			str += s + "\t";
		}
		return str;
	}

	public void parseLine(String line) {
		if (line.length() > 1) {
			count++;
			if (isSkip) {
				if (count < 3)
					return;
			}
			Vector<String> vector = evaluateLine(line);
			updateProcess(vector);
		}
	}

	public Vector<String> evaluateLine(String line) {
		Vector<String> newVect = new Vector<String>();
		Vector<String> vector = splitLine(line, " ");
		if (vector.size() == nCol) {
			first = vector.get(0);
			newVect.addAll(vector);
		} else if (vector.size() == nCol - 1) {
			newVect.add(first);
			newVect.addAll(vector);
		} else if (vector.size() == 1) {
			newVect.addAll(vector);
		}
		return newVect;
	}

	private void updateProcess(Vector<String> vector) {
		linux = false;
		isOne = false;
		if (process != null) {
			Object o[] = process.toArray();
			if (vector.size() > 2) {
				if ((vector.get(1).equalsIgnoreCase("PM")) || (vector.get(1).equalsIgnoreCase("AM"))) {
					linux = true;
				}
			}
			boolean flag = false;
			for (Object obj : o) {
				String str = (String) obj;
				if (vector.size() > 1) {
					if (linux) {
						if (str.equalsIgnoreCase(vector.get(2))) {
							flag = true;
						}
					} else if (str.equalsIgnoreCase(vector.get(1))) {
						flag = true;
					}
				} else if (vector.size() == 1) {
					isOne = true;
					if (str.equalsIgnoreCase("empty")) {
						flag = true;
					}
				}
			}
			if (!flag) {
				if (linux) {
					process.add(vector.get(2));
				} else if (isOne) {
					process.add("empty");
				} else {
					process.add(vector.get(1));
				}
				whole.add(new Vector<Vector<String>>());
			}
		} else {
			process = new Vector<String>();
			if (linux) {
				process.add(vector.get(3));
			} else if (isOne) {
				process.add("empty");
			} else {
				process.add(vector.get(1));
			}
			whole.add(new Vector<Vector<String>>(100, 20));
		}
		insertProcessed(vector);
	}

	public void insertProcessed(Vector<String> vector) {
		for (int i = 0; i < process.size(); i++) {
			if (linux) {
				if (process.get(i).equalsIgnoreCase(vector.get(2))) {
					whole.get(i).add(vector);
				}
			} else if (vector.size() > 1) {
				if (process.get(i).equalsIgnoreCase(vector.get(1))) {
					whole.get(i).add(vector);
				}
			} else if (vector.size() == 1) {
				if (process.get(i).equalsIgnoreCase("empty")) {
					whole.get(i).add(vector);
				}
			}
		}
	}

	public Vector<String> splitLine(String line, String delim) {
		Vector<String> vector = new Vector<String>();
		String str[] = line.split(delim);
		int cols = 0;
		for (String s : str) {
			if (!s.trim().equals("")) {
				vector.add(s);
				cols++;
				// System.out.println("Reading.. :"+cols);
				if (nCol < cols) {
					nCol++;
				}
			}
		}
		return vector;
	}
}
