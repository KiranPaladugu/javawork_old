import java.io.File;

public class Tailer {
	private static TailerThread tail;

	public static void makeview(ViewPanel panel, File file) {
		if (tail != null) {
			tail.stopThread();
			tail = null;
		}
		while (TailerThread.getNumberOfInstances() >= 1)
			try {
				ResultView.addToResultViewNLine("Waiting to terminate previous stream ..");
				Thread.sleep(200);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
		tail = new TailerThread(panel, file);
	}

	public Tailer() {

	}
}
