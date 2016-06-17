import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Exercise_9_24 extends JFrame {
	public static void main(String args[]) {
		Exercise_9_24 window = new Exercise_9_24();

		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		window.setSize(400, 120);
		window.show();
	}
	private Shape arrayOfShapes[] = new Shape[20];
	private int numberOfShapes;
	private JLabel xPosLabel, yPosLabel, xSizeLabel, ySizeLabel, shapeNumberLabel, fillColorRedLabel, fillColorGreenLabel,
			fillColorBlueLabel;
	private JTextField xPosField, yPosField, xSizeField, ySizeField, shapeNumberField, fillColorRedField, fillColorGreenField,
			fillColorBlueField;
	private int xPos, yPos, xSize, ySize, shapeNumber, fillColorRedNumber, fillColorGreenNumber, fillColorBlueNumber;

	private JButton submitButton;

	public Exercise_9_24() {
		super("Graphics, Shapes, and Exercise 9.24");

		Container c = getContentPane();

		c.setLayout(new FlowLayout());

		shapeNumberLabel = new JLabel("Enter 1 for Square, 2 for Circle, 3 for Rectangle, and 4 for a Triangle");
		shapeNumberField = new JTextField(10);
		xPosLabel = new JLabel("X Position");
		xPosField = new JTextField(10);
		yPosLabel = new JLabel("Y Position");
		yPosField = new JTextField(10);
		c.add(shapeNumberLabel);
		c.add(shapeNumberField);
		c.add(xPosLabel);
		c.add(xPosField);
		c.add(yPosLabel);
		c.add(yPosField);
		xSizeLabel = new JLabel("X Size");
		ySizeLabel = new JLabel("Y Size");
		xSizeField = new JTextField(10);
		ySizeField = new JTextField(10);
		c.add(xSizeLabel);
		c.add(xSizeField);
		c.add(ySizeLabel);
		c.add(ySizeField);
		fillColorRedLabel = new JLabel("Red");
		fillColorBlueLabel = new JLabel("Blue");
		fillColorGreenLabel = new JLabel("Green");
		fillColorRedField = new JTextField(10);
		fillColorBlueField = new JTextField(10);
		fillColorGreenField = new JTextField(10);
		c.add(fillColorRedLabel);
		c.add(fillColorRedField);
		c.add(fillColorGreenLabel);
		c.add(fillColorGreenField);
		c.add(fillColorBlueLabel);
		c.add(fillColorBlueField);

		// Graphics g = new Graphics();

		submitButton = new JButton("Submit Results");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getInputs();
				clearFields();
				createShape();
				numberOfShapes++;
				repaint();
				clearVariables();
			}
		});

		c.add(submitButton);
	}

	private void clearFields() {
		xPosField.setText("");
		yPosField.setText("");
		xSizeField.setText("");
		ySizeField.setText("");
		fillColorRedField.setText("");
		fillColorBlueField.setText("");
		fillColorGreenField.setText("");
		shapeNumberField.setText("");
	}

	private void clearVariables() {
		xPos = yPos = xSize = ySize = fillColorRedNumber = fillColorGreenNumber = fillColorBlueNumber = 0;
	}

	public void createShape() {

		switch (shapeNumber) {

		case 1:
			arrayOfShapes[numberOfShapes] = new Square(xPos, yPos, xSize, ySize, fillColorRedNumber, fillColorGreenNumber,
					fillColorBlueNumber);
			break;

		case 2:
			arrayOfShapes[numberOfShapes] = new Circle(xPos, yPos, xSize, ySize, fillColorRedNumber, fillColorGreenNumber,
					fillColorBlueNumber);
			break;

		case 3:
			arrayOfShapes[numberOfShapes] = new Rectangle(xPos, yPos, xSize, ySize, fillColorRedNumber, fillColorGreenNumber,
					fillColorBlueNumber);
			break;

		}
	}

	private void getInputs() {
		xPos = Integer.parseInt(xPosField.getText());
		yPos = Integer.parseInt(yPosField.getText());
		xSize = Integer.parseInt(xSizeField.getText());
		ySize = Integer.parseInt(ySizeField.getText());
		fillColorRedNumber = Integer.parseInt(fillColorRedField.getText());
		fillColorGreenNumber = Integer.parseInt(fillColorGreenField.getText());
		fillColorBlueNumber = Integer.parseInt(fillColorBlueField.getText());
		shapeNumber = Integer.parseInt(shapeNumberField.getText());
	}

	public void paint(Graphics g) {
		for (int i = 0; i < numberOfShapes; i++) {
			System.out.println("Drawing: " + numberOfShapes);
			arrayOfShapes[i].draw(g);
		}
	}

}
