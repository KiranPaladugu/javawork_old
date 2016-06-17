
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

public class Splitter {
	public static void main(String[] args) {
		Splitter tailer = new Splitter("TMFAgent.log", 1);
		tailer.startSlit();
		System.out.println("Total Files :" + tailer.getPages());
	}

	private FileChannel channel;
	private RandomAccessFile raf;
	private ByteBuffer buffer;
	private int capacity = 1048576;
	private int capCount = 3;
	private int flushCount = 0;
	private long totalWrite = 0;
	private int disCount = 0;
	private File file;
	private String path = null;

	public Splitter(String fileName) {
		file = new File(fileName);
		indntifyPatth();
	}
	
	public Splitter(File file,String outPath, int size){
		this.file = file;
		this.capCount = size;
		path = outPath+File.separator;
	}

	public Splitter(String fileName, int size) {
		file = new File(fileName);
		indntifyPatth();
		this.capCount = size;
	}

	public Splitter(File file) {
		this.file = file;
		indntifyPatth();
	}

	public Splitter(File file, int size) {
		this.file = file;
		indntifyPatth();
		this.capCount = size;
	}

	public void setFileSize(int size) {
		this.capCount = size;
	}

	public void setFileSize(double size) throws Exception {
		throw new Exception("Not Yet Implemented.");
	}

	public int getFileSize() {
		return capCount;
	}
	public long getBytesWriten(){
		return totalWrite;
	}
	public void startSlit() {
		try {
			raf = new RandomAccessFile(file, "r");
		} catch (FileNotFoundException e) {
			Logger.log(e.getMessage());
		}
		buffer = ByteBuffer.allocate(capacity * capCount);
		channel = raf.getChannel();
		try {
			Date date = new Date();
			Logger.log("Started on :" + date.toString());
			while ((channel.read(buffer)) != -1) {
				if (buffer.position() == buffer.capacity()) {
					flushCount++;
					writeBuffer(buffer);
					buffer.clear();
				}
			}
			buffer.flip();
			writeBuffer(buffer);
			date = new Date();
			Logger.log("Compleated on :" + date.toString());
			Logger.log("Total flushes :" + flushCount);
			Logger.log("Total bytes Writen:" + totalWrite);
			Logger.log("Total Files :" + getPages());
			channel.close();
			buffer = null;
			//closing channel and buffer.
			channel.close();
			raf.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void indntifyPatth() {
		String filepath = file.getAbsolutePath();
		int ind = filepath.lastIndexOf(File.separator);
		path = filepath.substring(0, ind);
		path +=File.separator;
	}

	public String getPath() {
		return path;
	}

	public void writeBuffer(ByteBuffer buffer) {
		disCount++;
		byte b[];
		if (buffer.capacity() != buffer.limit()) {
			int limit = buffer.limit();
			b = buffer.array();
			ByteBuffer buf = ByteBuffer.allocate(limit);
			buf.put(buffer);
			b = buf.array();
		} else {
			b = buffer.array();
		}
		String newName = getFileName(file.getName(), disCount);
		Logger.log("Now writing to .." + newName);
		File newFile = new File(path + newName);
		try {
			FileOutputStream fos = new FileOutputStream(newFile);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bos.write(b);
			totalWrite += b.length;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getFileName(String fileName, int flushCount) {
		String name = null;
		int lastIndex = -1;
		lastIndex = fileName.lastIndexOf('.');
		if (lastIndex != -1) {
			name = fileName.substring(0, lastIndex);
			name += "[";
			name += "" + flushCount;
			name += "]";
			name += fileName.substring(lastIndex, fileName.length());
			//name += ".spl";
		} else {
			name = fileName + "[" + flushCount + "]";
		}
		return name;
	}

	@SuppressWarnings("unused")
	private String getPath(String file) {
		return null;
	}

	public int getPages() {
		return flushCount + 1;
	}
}
