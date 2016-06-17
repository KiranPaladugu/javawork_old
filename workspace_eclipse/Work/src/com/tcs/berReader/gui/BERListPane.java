package com.tcs.berReader.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.framework.reg.Register;
import com.framework.reg.RegisterException;
import com.tcs.berReader.BERFileData;

public class BERListPane extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7322952642670934505L;
	private JList list;
	private JScrollPane scorll;
	private BerFileListModel model;
	private JPopupMenu popup;
	private UserOperations operations;
	private JMenuItem load, delete, refresh, writetoTxt;
	private Set<String> set = new HashSet<String>();
	private BerFileListHandler handler;

	public BERListPane() {
		Register.register(this);
		this.setLayout(new GridLayout(1, 1));
		init();
		add(scorll);
		try {
			operations = (UserOperations) Register.getCheckedObject(UserOperations.class);
		} catch (RegisterException e) {
		}
	}

	private void init() {
		list = new JList();
		scorll = new JScrollPane(list);
		model = new BerFileListModel();
		list.setModel(model);
		initPopup();
		list.add(popup);
		list.setCellRenderer(new ListRenderer());
		list.addMouseListener(new MouseListener() {

			
			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);

			}

			
			public void mousePressed(MouseEvent e) {

			}

			
			public void mouseExited(MouseEvent e) {

			}

			
			public void mouseEntered(MouseEvent e) {

			}

			
			public void mouseClicked(MouseEvent e) {

			}
		});		

		list.addListSelectionListener(new ListSelectionListener() {			
			public void valueChanged(ListSelectionEvent evt) {
				load();
			}
		});
	}

	private void load() {
		boolean flag = false;
		if(!ApplicationContext.isLoading()){
			ApplicationContext.setLoading(true);
			flag=true;
		}
		BerViewPane viewer = (BerViewPane) Register.getObject(BerViewPane.class);
		if (viewer != null) {
			try {
				if (list.getSelectedIndex() != -1) {
					BerFileListItem item = (BerFileListItem) list.getSelectedValue();
					if (operations.isLoadOnDemand() && item.getValue() == null) {
						File file = item.getKey();
						try {
							handler = (BerFileListHandler) Register.getCheckedObject(BerFileListHandler.class);
						} catch (RegisterException e) {
							e.printStackTrace();
						}
						BERFileData data = handler.getBERData(file);
						item.setValue(data);

					}
					viewer.displayBER(item.getValue());
				}
			} catch (Exception e) {
			}
		}
		if(flag){
			ApplicationContext.setLoading(false);
		}
	}

	private void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	private void initPopup() {
		popup = new JPopupMenu("Options.. !");
		load = new JMenuItem("ShowBerDetails..");
		load.addActionListener(new ActionListener() {

			
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedIndex() != -1) {
					BERFileData data = ((BerFileListItem) list.getSelectedValue()).getValue();
					BERFileDisplay display = new BERFileDisplay(data);
					display.displayBer();

				}
			}
		});
		delete = new JMenuItem("Delete..");
		delete.addActionListener(new ActionListener() {

			
			public void actionPerformed(ActionEvent arg0) {
				Object objs[] = list.getSelectedValues();
				for (Object obj : objs) {
					if (obj instanceof BerFileListItem) {
						File fremove = ((BerFileListItem) obj).getKey();
						set.remove(fremove.getAbsoluteFile());
						model.removeElement(obj);
						Register.dump();
						BerFileListHandler listHandler = (BerFileListHandler) Register.getObject(BerFileListHandler.class);
						if (listHandler != null) {
							handler.remove(fremove);
						}
					}
				}
			}
		});

		refresh = new JMenuItem("Refresh..");
		writetoTxt = new JMenuItem("Write to text file..");
		writetoTxt.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				Object objs[] = list.getSelectedValues();
				if (operations != null) {
					for (Object obj : objs) {
						if (obj instanceof BerFileListItem) {
							BerFileListItem itm = (BerFileListItem) obj;
							String path = MessageHandler.dispalyInputMessage("Enter full for text file for :\n"
									+ itm.getKey().getAbsolutePath());
							if (path != null) {
								boolean flag = operations.writeToTextFile(itm, path);
								if (operations.isLoadOnDemand() && !operations.isKeepOnLoad() && !flag) {
									break;
								}
							} 
						}
					}
				}
			}
		});

		// popup.add(load);
		popup.add(delete);
		popup.add(refresh);
		popup.addSeparator();
		popup.add(writetoTxt);
	}

	public void addToList(File file, BERFileData data) {
		BerFileListItem item = new BerFileListItem(file, data);
		model.add(item);
	}

	public void addToList(BerFileListItem item) {
		if (!set.contains(item.getKey().getAbsolutePath())) {
			model.add(item);
			set.add(item.getKey().getAbsolutePath());
		}
	}

	public BERFileData getSelectedItem() {
		return ((BerFileListItem) list.getSelectedValue()).getValue();
	}

}
