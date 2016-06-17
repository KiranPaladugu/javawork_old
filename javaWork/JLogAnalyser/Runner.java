import reg.Register;

public class Runner {
	public static void main(String[] args) {		
		ViewerFrame frame = new ViewerFrame();
		Register.register(ViewerFrame.class, frame);
	}
}
