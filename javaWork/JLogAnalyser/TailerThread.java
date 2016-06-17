import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TailerThread implements Runnable {
	private File file;
	private ViewPanel panel;
	private Thread t;
	private int capacity = 524288;
	private int capCount = 1;
	@SuppressWarnings("unused")
	private int flushCount = 0;
	private long totalWrite = 0;
	private static int instances = 0;
	private FileChannel channel;
	private RandomAccessFile raf;
	private ByteBuffer buffer;
	@SuppressWarnings("unused")
	private int disCount = 0;
	private static volatile boolean pause = false;

	public static int getNumberOfInstances() {
		return instances;
	}

	private static volatile boolean stop = false;

	public TailerThread(ViewPanel panel, File file) {
		this.panel = panel;
		this.file = file;
		t = new Thread(this);
		t.start();
	}

	public void run() {
		instances++;
		stop =false;
		try {
			raf = new RandomAccessFile(file, "r");
		} catch (FileNotFoundException e) {
			ResultView.addToResultViewNLine("" + e.getLocalizedMessage());
		}
		try {
			buffer = ByteBuffer.allocate(capacity * capCount);
			channel = raf.getChannel();
			while ((!stop) && ((channel.read(buffer)) != -1)) {				
				if (!pause) {
					if (buffer.position() == buffer.capacity()) {
						flushCount++;
						writeBuffer(buffer);
						buffer.clear();
					}
				} else {
					Thread.sleep(1000);
				}
			}
			buffer.flip();
			writeBuffer(buffer);
			channel.close();
			buffer = null;
			tailMe();
			raf.close();

		} catch (IOException e) {
			ResultView.addToResultViewNLine(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (Throwable t) {
			if (t.getLocalizedMessage() != null)
				System.gc();
			ResultView.addToResultViewNLine("HE: " + t.getLocalizedMessage());
		}
		/*
		 * try { fr = new FileReader(file); br = new BufferedReader(fr);
		 * instances++; while (!stop) { String msg = br.readLine(); if (msg ==
		 * null) { t.join(1000); } else { panel.appendToLogText(msg + "\n"); } }
		 * fr.close(); br.close(); } catch (FileNotFoundException e) {
		 * System.out.println(file.getName() + " * * * File Not Found * * *");
		 * ResultView.addToResultViewNLine("" + e); } catch (IOException e) {
		 * System.out.println(file.getName() + " * * * Unable to read * * *");
		 * ResultView.addToResultViewNLine("" + e); } catch
		 * (InterruptedException e) { ResultView.addToResultViewNLine("" + e); }
		 */finally {
			instances--;
		}

	}

	public static void setPause(boolean value) {
		pause = value;
	}

	private void tailMe() {
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "rwd");
			FileChannel fileChannel = raf.getChannel();
			fileChannel.position(totalWrite);
			ByteBuffer buf = ByteBuffer.allocate(capacity * capCount);
			while (!stop) {				
				if (!pause) {
					fileChannel.read(buf, totalWrite);
					byte b[];
					b = buf.array();
					int n = buf.position();
					buf.rewind();
					String msg = new String(b, 0, n);
					if ((msg == null) || (msg.equalsIgnoreCase(""))) {
						t.join(100);
					} else {
						panel.appendToLogText(msg);
						totalWrite += n;
					}
				} else {
					Thread.sleep(1000);
				}
			}			
			fileChannel.close();
		} catch (FileNotFoundException e) {
			System.out.println(file.getName() + " * * * File Not Found * * *");
			ResultView.addToResultViewNLine("ss" + e);
		} catch (IOException e) {
			System.out.println(file.getName() + " * * * Unable to read * * *");
			ResultView.addToResultViewNLine("ss" + e);
		} catch (InterruptedException e) {
			ResultView.addToResultViewNLine("" + e);
		}

	}

	public synchronized void writeBuffer(ByteBuffer buffer) {
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
		panel.appendToLogText(new String(b));
		totalWrite += b.length;
	}

	public void stopThread() {
		stop = true;
	}
	public static void stop(){
		stop = true;	
	}
}
