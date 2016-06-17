import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

public class RecentStorer {
	public static Vector<File> readRecent() {
		if (store == null) {
			store = new RecentStorer();
		}
		RecentData data = store.read();
		if (data != null)
			return data.getVector();
		else
			return null;
	}
	public static boolean storeToRecent(File f) {
		boolean flag = false;
		if (store == null) {
			store = new RecentStorer();
		}
		if (store.isDubplicate(f)) {
			return false;
		}
		RecentData data = store.read();
		if (data != null) {
			Vector<File> vector = data.getVector();
			vector.add(f);
			data.setVector(vector);
			flag = store.write(data);
		} else {
			data = new RecentData();
			Vector<File> d = new Vector<File>();
			d.add(f);
			data.setVector(d);
			flag = store.write(data);
		}
		return flag;
	}
	private String fileName = "recent";
	// private File file;
	private FileInputStream fis;
	private ObjectInputStream ois;
	private static RecentStorer store;

	private ObjectOutputStream oos;

	private FileOutputStream fos;

	public RecentStorer() {
		File file = new File(fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private boolean isDubplicate(File file) {
		boolean flag = false;
		Vector<File> vector = readRecent();
		if (vector != null) {
			for (int i = 0; i < vector.size(); i++) {
				File f = vector.get(i);
				if (f.getAbsolutePath().equals(file.getAbsolutePath())) {
					return true;
				}
			}
		}
		return flag;
	}

	public RecentData read() {
		RecentData data = null;
		File file = new File(fileName);
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			Object obj = null;
			while ((obj = ois.readObject()) != null) {
				if (obj instanceof RecentData) {
					data = (RecentData) obj;
				}
			}
			fis.close();
			ois.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
			// System.out.println("*************** FROM E1 *********************");
			//ResultView.addToResultViewNLine("" + e1);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
			// System.out.println("*************** FROM cE *********************");
			ResultView.addToResultViewNLine("" + e1);
		} catch (Exception e1) {
			// e.printStackTrace();
			// System.out.println("*************** FROM E *********************");
			ResultView.addToResultViewNLine("" + e1);
		} finally {

		}
		return data;
	}

	public boolean write(RecentData data) {
		File file = new File(fileName);
		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(data);
			ois.close();
			fis.close();
			fos.close();
			oos.close();
			return true;
		} catch (FileNotFoundException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			ResultView.addToResultViewNLine("" + e);
		} catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			ResultView.addToResultViewNLine("" + e);
		} catch (Exception e) {

		}
		return false;

	}
}
