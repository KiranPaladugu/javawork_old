import java.io.File;
import java.io.Serializable;
import java.util.Vector;

public class RecentData implements Serializable {

	private static final long serialVersionUID = -1532272530509222744L;
	private Vector<File> vector;

	public RecentData() {
		vector = new Vector<File>();
	}

	public RecentData(Vector<File> vector) {
		super();
		this.vector = vector;
	}

	public Vector<File> getVector() {
		return vector;
	}

	public void setVector(Vector<File> vector) {
		this.vector = vector;
	}

}
