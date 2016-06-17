package com.tcs.berReader.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.framework.reg.Register;
import com.framework.utils.License;
import com.marconi.fusion.X36.X36PortInformation;
import com.tcs.ber.resource.PropertyFinder;
import com.tcs.ber.resource.ResourceFinder;

public class DisplayDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6710201743416100961L;

	private static UserInterface frame;
	private static LocationInfo loc;
	private static JLabel lbl_shelf = new JLabel("Shelf :");
	private static JLabel lbl_card = new JLabel("Card :");
	private static JLabel lbl_port = new JLabel("Port :");
	private static JTextField txt_shelf = new JTextField("1");
	private static JTextField txt_card = new JTextField("1");
	private static JTextField txt_port = new JTextField("1");
	private static JButton btn_Ok = new JButton("OK");
	private static JButton btn_cancel, btn_close;
	private static JDialog dialog;
	private static final String iconName = "BERViewer.Icon.name.MESSAGE";

	private static UserInterface getUserInterface() {
		if (frame == null) {
			frame = (UserInterface) Register.getObject(UserInterface.class);
		}
		return frame;
	}

	public static JDialog dispaly(String string) {
		dialog = new JDialog(getUserInterface().getFrame());
		
		
		final JTextArea area = new JTextArea();
		area.setEditable(false);
		final JPopupMenu popup = new JPopupMenu();
		JMenuItem selectAll = new JMenuItem("SelectAll");
		selectAll.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				area.selectAll();
			}
		});
		JMenuItem copy = new JMenuItem("CopySelected");
		copy.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				area.copy();
			}
		});
		
		JMenuItem copyAll = new JMenuItem("CopyAll");
		copyAll.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				int x = area.getSelectionStart();
				int y = area.getSelectionEnd();				
				area.selectAll();
				area.copy();
				if(x!=-1 && y!=-1){
					area.select(x, y);
				}
			}
		});
		
		popup.add(selectAll);
		popup.addSeparator();
		popup.add(copy);
		popup.add(copyAll);
		JScrollPane scroll = new JScrollPane(area);
		area.setText(string);
		area.add(popup);
		
		area.addMouseListener(new MouseListener() {
			
			
			public void mouseReleased(MouseEvent arg0) {
				if(arg0.isPopupTrigger()){
					popup.show(area, arg0.getX(), arg0.getY());
				}
			}
			
			
			public void mousePressed(MouseEvent arg0) {
				
			}
			
			
			public void mouseExited(MouseEvent arg0) {
				
			}
			
			
			public void mouseEntered(MouseEvent arg0) {
				
			}
			
			
			public void mouseClicked(MouseEvent arg0) {
				
			}
		});
		dialog.setLayout(new GridLayout());
		dialog.add(scroll);
		dialog.setModal(true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setBounds(setDialogLocation(600, 500));
		dialog.setVisible(true);
		try {
			dialog.setIconImage(ResourceFinder.getImageWithName(PropertyFinder.getPropertyWithName(iconName)).getImage());
		} catch (IOException e) {
		}
		return dialog;
	}

	public static LocationInfo showLocationInputDialog(final NodeConfigQueryHandler ncq) {
		loc = null;
		final JDialog dialog = prepareGetPortInfoDialog();

		btn_Ok.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				X36PortInformation portInfo = getPortInfo(ncq, dialog);
				if (portInfo != null)
					dispalyMessage(portInfo.toString(), "PortInforamtion!.");
			}
		});

		btn_cancel.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				txt_shelf.setText("");
				txt_card.setText("");
				txt_port.setText("");

			}
		});

		return null;

	}

	private static JDialog prepareGetPortInfoDialog() {
		JPanel input = new JPanel();
		input.setLayout(new GridLayout(3, 2));
		JPanel btn = new JPanel();
		dialog = new JDialog(getUserInterface().getFrame());
		lbl_shelf = new JLabel("Shelf :");
		lbl_card = new JLabel("Card :");
		lbl_port = new JLabel("Port :");
		txt_shelf = new JTextField("1");
		txt_card = new JTextField("1");
		txt_port = new JTextField("1");
		btn_Ok = new JButton("OK");
		btn_cancel = new JButton("Cancel");
		btn_close = new JButton("Close");
		btn_close.addActionListener(new ActionListener() {

			
			public void actionPerformed(ActionEvent arg0) {
				dialog.dispose();
			}
		});
		dialog.setLayout(new FlowLayout());
		input.add(lbl_shelf);
		input.add(txt_shelf);
		input.add(lbl_card);
		input.add(txt_card);
		input.add(lbl_port);
		input.add(txt_port);
		btn.add(btn_Ok);
		btn.add(btn_cancel);
		btn.add(btn_close);
		dialog.add(input);
		dialog.add(btn);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setTitle("Ports!");
		dialog.setBounds(setDialogLocation(200, 135));
		dialog.setResizable(false);
		try {
			dialog.setIconImage(ResourceFinder.getImageWithName(PropertyFinder.getPropertyWithName(iconName)).getImage());
		} catch (IOException e) {
		}
		dialog.setVisible(true);
		return dialog;
	}

	/**
	 * @param ncq
	 * @param dialog
	 */
	private static X36PortInformation getPortInfo(final NodeConfigQueryHandler ncq, final JDialog dialog) {
		loc = new LocationInfo();
		try {
			String shelf = txt_shelf.getText().trim();
			String card = txt_card.getText().trim();
			String port = txt_card.getText().trim();
			if (!shelf.equals("") && !shelf.equalsIgnoreCase("0")) {
				loc.setShelf(Integer.parseInt(txt_shelf.getText().trim()));
				if (!card.equals("") && !card.equalsIgnoreCase("0")) {
					loc.setCard(Integer.parseInt(txt_card.getText().trim()));
					if (!port.equals("") && !port.equalsIgnoreCase("0")) {
						loc.setPort(Integer.parseInt(txt_port.getText().trim()));
						X36PortInformation portInfo = ncq.getPortInfo(loc);
						if (portInfo == null) {
							MessageHandler.displayErrorMessage("Unable to obtain Required Information.");
						} else {
							dialog.dispose();
							return portInfo;
						}
					} else {
						MessageHandler.displayErrorMessage("Invalid Port.");
					}
				} else {
					MessageHandler.displayErrorMessage("Invalid Card.");
				}
			} else {
				MessageHandler.displayErrorMessage("Invalid Shelf.");
			}

		} catch (Exception e) {
			loc = null;
			MessageHandler.displayErrorMessage("Invalid Input..");
		}
		return null;
	}

	public static void dispalyMessage(String string, String title) {
		JDialog dialog = new JDialog(getUserInterface().getFrame());
		final JTextArea area = new JTextArea();
		area.setEditable(false);
		JScrollPane scroll = new JScrollPane(area);		
		area.setText(string);
		final JPopupMenu popup = new JPopupMenu();
		JMenuItem selectAll = new JMenuItem("SelectAll");
		selectAll.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				area.selectAll();
			}
		});
		JMenuItem copy = new JMenuItem("CopySelected");
		copy.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				area.copy();
			}
		});
		JMenuItem copyAll = new JMenuItem("CopyAll");
		copyAll.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				int x = area.getSelectionStart();
				int y = area.getSelectionEnd();				
				area.selectAll();
				area.copy();
				if(x!=-1 && y!=-1){
					area.select(x, y);
				}
			}
		});
		
		popup.add(selectAll);
		popup.addSeparator();
		popup.add(copy);
		popup.add(copyAll);
		area.addMouseListener(new MouseListener() {
			
			
			public void mouseReleased(MouseEvent arg0) {
				if(arg0.isPopupTrigger()){
					popup.show(area, arg0.getX(), arg0.getY());
				}
			}
			
			
			public void mousePressed(MouseEvent arg0) {
				
			}
			
			
			public void mouseExited(MouseEvent arg0) {
				
			}
			
			
			public void mouseEntered(MouseEvent arg0) {
				
			}
			
			
			public void mouseClicked(MouseEvent arg0) {
				
			}
		});
		dialog.setLayout(new GridLayout());
		dialog.add(scroll);
		dialog.setModal(true);
		dialog.setTitle(title);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setBounds(setDialogLocation(600, 400));
		try {
			dialog.setIconImage(ResourceFinder.getImageWithName(PropertyFinder.getPropertyWithName(iconName)).getImage());
		} catch (IOException e) {
		}
		dialog.setVisible(true);

	}

	public static LocationInfo getLocaionInfo() {
		return loc;
	}

	public static Rectangle setDialogLocation(int width, int height) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width - width) / 2;
		int y = (dim.height - height) / 2;
		Rectangle rect = new Rectangle(x, y, width, height);
		return rect;
	}

	public static void displayCreditsMessage() {
		String name = null;
		String startDate = null;
		String endDate = null;
		License lic = null;
		String rdays = null;
		String version = "             *BERViewer v1.0a(alfa)* ";
		try {
			Object obj = Register.getObject(License.class.getClass());
			if (obj != null) {
				lic = (License) obj;
				name = lic.getName();
				startDate = lic.startDate().toString();
				endDate = lic.endDate().toString();
				rdays = "" + lic.getRemainingDays();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (endDate == null) {
			endDate = "UNKNOWN";
			rdays = "UNKNOWN";
		}
		if (lic == null) {
			name = "Cheater !";
			endDate = "UNKNOWN";
			startDate = "UNKNOWN";
			rdays = "UNKNOWN";
			version ="             *BERViewer PIRATED!!!* ";
		}
		String msg = version+"\n\nDeveloped by : Kiran\nEmail :paladugu.kiran@tcs.com \n\nLicensed to :"
				+ name + "\nStart Date :" + startDate + "\nValid Till  :" + endDate + "\nRemainingDays :" + rdays;
		JOptionPane.showMessageDialog(getUserInterface().getFrame(), msg, "Credits!", JOptionPane.INFORMATION_MESSAGE);
	}

}
