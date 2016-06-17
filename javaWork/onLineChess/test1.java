import javax.swing.*;
import java.awt.*;

public class test1 {
	public test1() {
		JFrame frame = new JFrame("Drag");
		Container container = frame.getContentPane();
		JPanel panel = new JPanel(new GridLayout(2, 1));
		JPanel panel1 = new JPanel(new BorderLayout());
		JPanel panel2 = new JPanel();
		JLabel label = new JLabel(new ImageIcon("ing/00.png"));
		panel1.add(label);
		panel1.setBackground(Color.red);
		panel2.setBackground(Color.gray);
		panel.add(panel1);
		panel.add(panel2);
		container.add(panel);
		frame.setSize(300, 400);
		frame.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new test1();
	}
}
