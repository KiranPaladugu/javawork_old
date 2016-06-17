package utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

public class TailerClass {
	public static void main(String[] args) {
		TailerClass tailer = new TailerClass();
		System.out.println("Total Files :" + tailer.getPages());
		for (int i = 1; i <= tailer.getPages(); i++) {
			System.out.print(tailer.getFileName("TMFAgent", i));
			System.out.print("\t" + new File(tailer.getFileName("TMFAgent", i)).getAbsolutePath() + "\n");
		}
	}
	private FileChannel channel;
	private RandomAccessFile raf;
	private ByteBuffer buffer;
	private int capacity = 1048576;
	private int capCount = 3;
	private int flushCount = 0;
	private int totalWrite = 0;

	private int disCount = 0;

	public TailerClass() {
		File file = new File("TMFAgent.log");
		try {
			raf = new RandomAccessFile(file, "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		buffer = ByteBuffer.allocate(capacity * capCount);
		channel = raf.getChannel();
		try {
			Date date = new Date();
			System.out.println("Started on :" + date.toString());
			while ((channel.read(buffer)) != -1) {
				if (buffer.position() == buffer.capacity()) {
					flushCount++;
					displayBuffer(buffer);
					buffer.clear();
				}
			}
			buffer.flip();
			displayBuffer(buffer);
			date = new Date();
			System.out.println("Compleated on :" + date.toString());
			System.out.println("Total flushes :" + flushCount);
			System.out.println("Total bytes Writen:" + totalWrite);
			channel.close();
			buffer = null;
			raf.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void displayBuffer(ByteBuffer buffer) {
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
		File file = new File("TMFAgent[" + disCount + "].log");
		try {
			FileOutputStream fos = new FileOutputStream(file);
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
		} else {
			name = fileName + "[" + flushCount + "]";
		}
		return name;
	}

	public int getPages() {
		return flushCount + 1;
	}
}
