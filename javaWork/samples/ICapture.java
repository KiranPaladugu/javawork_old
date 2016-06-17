import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

public class ICapture extends JFrame implements ActionListener, MouseMotionListener, MouseListener {
	private static final long serialVersionUID = 1L;
	private ICapture.FullScreenWindow fullScreenWindow;
	private JButton captureScreen;
	private JButton exitButton;
	private Container c;
	private JPanel buttonPanel;
	private JPanel labelPanel;
	private JLabel title;
	private Dimension screenDimension;
	Point location;
	MouseEvent pressed;

	public ICapture() throws Exception {
		super("ICapture");
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (InstantiationException localInstantiationException) {
		} catch (ClassNotFoundException localClassNotFoundException) {
		} catch (UnsupportedLookAndFeelException localUnsupportedLookAndFeelException) {
		} catch (IllegalAccessException localIllegalAccessException) {
		}
		this.fullScreenWindow = new ICapture.FullScreenWindow();

		this.captureScreen = new JButton("Capture");
		this.captureScreen.setToolTipText("Click this button to capture the screenshots. Press [Esc] when done.");

		this.exitButton = new JButton("    Exit    ");

		this.c = getContentPane();

		this.c.setLayout(new BorderLayout());

		this.labelPanel = new JPanel();
		this.title = new JLabel("ICapture");
		this.title.setFont(new Font("TrebuchetMs", 1, 14));
		this.title.setForeground(new Color(255, 115, 21));
		this.labelPanel.add(this.title);

		this.buttonPanel = new JPanel();
		this.buttonPanel.setLayout(new FlowLayout());
		this.buttonPanel.add(this.captureScreen);
		this.buttonPanel.add(this.exitButton);

		Border b = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(255, 115, 21));
		((JPanel) this.c).setBorder(b);

		this.c.add(new JPanel(), "North");
		this.c.add(this.labelPanel, "Center");
		this.c.add(this.buttonPanel, "South");

		this.captureScreen.addActionListener(this);
		this.exitButton.addActionListener(this);
		this.screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((int) this.screenDimension.getWidth() - 205, (int) this.screenDimension.getHeight() - 160);
		setSize(200, 100);
		addMouseListener(this);
		addMouseMotionListener(this);
		setUndecorated(true);
		setAlwaysOnTop(true);

		URL trayIconURL = ICapture.class.getResource("trayIcon.gif");
		// Image trayIconImage = ImageIO.read(trayIconURL);
		// setIconImage(trayIconImage);

		setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		new ICapture();
	}

	public void mouseDragged(MouseEvent me) {
		this.location = getLocation(this.location);
		int x = this.location.x - this.pressed.getX() + me.getX();
		int y = this.location.y - this.pressed.getY() + me.getY();
		setLocation(x, y);
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
		this.pressed = arg0;
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	public void mouseMoved(MouseEvent arg0) {
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == this.exitButton) {
			if (this.fullScreenWindow != null)
				this.fullScreenWindow.dispose();
			System.exit(0);
		} else if (arg0.getSource() == this.captureScreen) {
			setState(1);
			if (!this.fullScreenWindow.isVisible())
				this.fullScreenWindow.captureScreen();
			this.fullScreenWindow.setVisible(true);
		}
	}

	private class FullScreenWindow extends JFrame implements MouseListener, MouseMotionListener, KeyListener {
		private Dimension screenDimension;
		private static final long serialVersionUID = 1L;
		private int X1;
		private int Y1;
		private int X2;
		private int Y2;
		private JPanel drawPanel;
		private BufferedImage screenImage;
		private JLabel statusLabel;
		private JPanel statusLabelPanel;
		private JPanel templabelPanel;
		private boolean clearRect;

		public FullScreenWindow() {
			this.screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
			setSize((int) this.screenDimension.getWidth(), (int) this.screenDimension.getHeight());
			setUndecorated(true);
			this.drawPanel = new JPanel() {
				private static final long serialVersionUID = 1L;

				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(Color.RED);
					g.setFont(new Font("Serif", 1, 12));
					if (!ICapture.FullScreenWindow.this.clearRect)
						g.drawRect(ICapture.FullScreenWindow.this.X1, ICapture.FullScreenWindow.this.Y1,
								ICapture.FullScreenWindow.this.X2 - ICapture.FullScreenWindow.this.X1,
								ICapture.FullScreenWindow.this.Y2 - ICapture.FullScreenWindow.this.Y1);
				}
			};
			this.drawPanel.setOpaque(false);
			setContentPane(new JPanel() {
				private static final long serialVersionUID = 1L;

				public void paintComponent(Graphics g) {
					if (ICapture.FullScreenWindow.this.screenImage != null)
						g.drawImage(ICapture.FullScreenWindow.this.screenImage, 0, 0,
								(int) ICapture.FullScreenWindow.this.screenDimension.getWidth(),
								(int) ICapture.FullScreenWindow.this.screenDimension.getHeight(), this);
				}
			});
			this.drawPanel.setLayout(new BorderLayout());

			this.statusLabel = new JLabel("Drag the mouse to capture the screen shot. Press [Esc] when done.");
			this.statusLabel.setBackground(Color.WHITE);
			this.statusLabel.setForeground(Color.RED);
			this.statusLabel.setFont(new Font("SansSerif", 1, 12));

			this.statusLabelPanel = new JPanel();
			this.statusLabelPanel.setLayout(new BorderLayout());
			this.statusLabelPanel.add(this.statusLabel, "East");

			this.templabelPanel = new JPanel();
			this.templabelPanel.add(this.statusLabelPanel);

			this.drawPanel.add(this.templabelPanel, "South");

			setLayout(new GridLayout(1, 1));
			add(this.drawPanel);

			setForeground(Color.RED);

			setAlwaysOnTop(true);
			addKeyListener(this);
			addMouseListener(this);
			addMouseMotionListener(this);
		}

		public void setVisible(boolean visibility) {
			super.setVisible(visibility);
		}

		public void captureScreen() {
			try {
				this.screenImage = new Robot().createScreenCapture(new Rectangle(this.screenDimension));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void mouseClicked(MouseEvent arg0) {
		}

		public void mouseEntered(MouseEvent arg0) {
		}

		public void mouseExited(MouseEvent arg0) {
		}

		public void mousePressed(MouseEvent arg0) {
			this.X1 = arg0.getX();
			this.Y1 = arg0.getY();
			this.clearRect = false;
		}

		protected void updateRect() {
			if ((this.X1 < this.X2) && (this.Y1 < this.Y2))
				this.drawPanel.repaint();
		}

		private void clearRect() {
			this.clearRect = true;
			this.drawPanel.repaint();
		}

		public void mouseReleased(MouseEvent arg0) {
			if ((this.X1 < this.X2) && (this.Y1 < this.Y2) && (this.X2 - this.X1 > 10) && (this.Y2 - this.Y1 > 10)) {
				clearRect();
				for (int i = 1; i < 2147483647;)
					try {
						File imageFile = new File("ScreenShot" + i + ".jpg");
						if (!imageFile.exists()) {
							imageFile.createNewFile();
							ImageIO.write(this.screenImage.getSubimage(this.X1, this.Y1, this.X2 - this.X1, this.Y2 - this.Y1),
									"jpg", imageFile);
						}
					} catch (IOException ioe) {
						ioe.printStackTrace();

						i++;
					}
			}
		}

		public void keyPressed(KeyEvent arg0) {
			if (arg0.getKeyCode() == 27)
				setVisible(false);
		}

		public void keyReleased(KeyEvent arg0) {
		}

		public void keyTyped(KeyEvent arg0) {
		}

		public void mouseDragged(MouseEvent arg0) {
			this.X2 = arg0.getX();
			this.Y2 = arg0.getY();
			updateRect();
		}

		public void mouseMoved(MouseEvent arg0) {
		}
	}
}