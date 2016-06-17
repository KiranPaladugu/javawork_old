package chat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class ChatClient {
	private JTextArea incoming;
	private JTextField outgoing;
	private BufferedReader reader;
	private PrintWriter writer;
	private Socket socket;
	private JFrame frame;
	private boolean active=false;
	private volatile boolean running=true;

	public static void main(String[] args) {
		ChatClient client = new ChatClient();
		client.go();
	}

	public void go() {
		frame = new JFrame("JOC chat client..");
		JPanel mainPanel = new JPanel();
		incoming = new JTextArea(15, 50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		outgoing = new JTextField(40);
		outgoing.addKeyListener(new ClientKeyListener());
		JButton send = new JButton("Send");
		send.addActionListener(new SendButtonListener());
		mainPanel.add(qScroller);
		mainPanel.add(outgoing);
		mainPanel.add(send);
		setUpNetworking();
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();

		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(650, 400);
		frame.setVisible(true);
		frame.addWindowListener(new clientWindowListener());
	}

	public void setUpNetworking() {
		try {
			socket = new Socket("172.16.49.86", 5000);
			InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(socket.getOutputStream());
			System.out.println("Network Established...");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Unable to Connect to server.", "ERROR!!", JOptionPane.ERROR_MESSAGE);			
			try{
			if (socket.isConnected()) {
				try {
					socket.close();
				} catch (IOException e1) {
					System.out.println("Exiting....");
					System.exit(0);
				}
			}else{
				System.out.println("Exiting....");
				System.exit(0);
			}
			}catch(Exception ex){
				System.out.println("Exiting....");
				System.exit(0);
			}
		}
	}

	public class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			sendMessage();
		}
	}

	public void sendMessage() {
		try {
			writer.println(outgoing.getText().trim());
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();			
		}
		outgoing.setText("");
		outgoing.requestFocus();
	}

	public class IncomingReader implements Runnable {
		public void run() {
			String message;
			System.out.println("started Client.");
			try {
				while( ((message = reader.readLine()) != null) && running){
					System.out.println("Read:" + message);
					incoming.append(message + "\n");
					incoming.setCaretPosition(incoming.getText().length());
					notifyUser();
				}
			} catch (Exception e) {
				System.out.println("Connection lost...");
			}
			System.out.println("Closing Client ReaderChannel.");
		}
	}

	public class clientWindowListener implements WindowListener {

		public void windowDeactivated(WindowEvent e) {
		}

		public void windowDeiconified(WindowEvent e) {
		}

		public void windowIconified(WindowEvent e) {
		}

		public void windowOpened(WindowEvent e) {
		}

		public void windowActivated(WindowEvent e) {
		}

		public void windowClosed(WindowEvent e) {
		}

		public void windowClosing(WindowEvent e) {
			try {
				running = false;
				socket.close();
			} catch (IOException e1) {
			}
			System.exit(0);
		}
	}

	public class ClientKeyListener implements KeyListener {

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 10) {				
				sendMessage();
			}
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}

	}
	public void notifyUser(){
		frame.toFront();
	}

}
