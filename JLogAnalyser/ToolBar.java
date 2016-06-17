import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class ToolBar extends JToolBar{
	/**
	 * 
	 */
	private static final long serialVersionUID = -657894987268790763L;
	private JButton clear;
	private JButton undo,redo,delete,copy,cut,paste,backGround,foreGround;
	private JToggleButton sLock,editable,bold;
	private ToolBarPopup popup;
	private ViewPanel view;
	private PopupMenu popupMenu;
	public ToolBar(ViewPanel view) {
		this.view = view;
		init();
		this.requestFocusInWindow();
	}
	private void init(){
		clear = new JButton();
		Image clear_image= Toolkit.getDefaultToolkit().getImage("img"+File.separator+"clear.png");
		ImageIcon clear_img= new ImageIcon(clear_image);
		clear.setIcon(clear_img);
		clear.setToolTipText("Clear");
		clear.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				view.logView.getAccessibleContext().getAccessibleEditableText().setTextContents("");
			}
		});
		undo = new JButton();		
		undo.setEnabled(false);
		undo.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				performUndoOperatiion();
			}
		});
		undo.setToolTipText("Undo");
		undo.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img"+File.separator+"undo.png")));
		redo = new JButton();
		redo.setEnabled(false);
		redo.setToolTipText("Redo");
		redo.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				performRedoOperatiion();
			}
		});
		redo.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img"+File.separator+"redo.png")));
		delete = new JButton();
		delete.setToolTipText("Delete");
		delete.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				performDeleteOperation();
			}
		});
		delete.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img"+File.separator+"edit-delete.png")));
		copy = new JButton();
		copy.setToolTipText("Copy");
		copy.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				performCopyOperation();
			}
		});
		copy.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img"+File.separator+"copy.png")));
		cut = new JButton();
		cut.setToolTipText("Cut");
		cut.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				performCutOperation();
			}
		});
		cut.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img"+File.separator+"cut.png")));
		paste = new JButton();
		paste.setToolTipText("Paste");
		paste.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				performPasteOperation();
			}
		});
		paste.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img"+File.separator+"edit-paste.png")));
		sLock = new JToggleButton();
		sLock.setToolTipText("Lock Scroll");
		sLock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setScrollLockOperation(((JToggleButton) e.getSource()).isSelected());
			}
		});
		sLock.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img"+File.separator+"lock.png")));
		editable = new JToggleButton();
		editable.setToolTipText("Set Editable");
		editable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setEditableOperation(((JToggleButton) e.getSource()).isSelected());
			}
		});
		editable.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img"+File.separator+"edit.png")));
		backGround = new JButton();
		backGround.setToolTipText("Back Ground Color");
		backGround.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(null, "Choose Back ground", Color.white);
				view.logView.setBackground(color);
			}
		});
		backGround.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img"+File.separator+"background.png")));
		foreGround = new JButton();
		foreGround.setToolTipText("Fore Ground Color");
		foreGround.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(null, "Choose Fore ground", Color.BLACK);
				view.logView.setForeground(color);
			}
		});
		foreGround.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img"+File.separator+"color.png")));
		
		bold = new JToggleButton();
		bold.setToolTipText("set Text to Bold");
		bold.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				Font dFont = view.logView.getFont();
				Font font =null;
				if(bold.isSelected()){
					font=new Font(dFont.getFontName(), Font.BOLD, 13);
				}  else {
					JTextArea t = new JTextArea();
					font = t.getFont();
				}
				view.logView.setFont(font);
				
			}
		});
		bold.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img"+File.separator+"bold.png")));				
		this.add(undo);
		this.add(redo);
		this.add(cut);
		cut.setFocusable(false);
		this.add(copy);
		copy.setFocusable(false);
		this.add(paste);
		paste.setFocusable(false);
		this.add(delete);
		delete.setFocusable(false);
		this.add(clear);
		clear.setFocusable(false);
		this.add(sLock);
		sLock.setFocusable(false);
		this.add(editable);
		editable.setFocusable(false);
		this.add(backGround);
		backGround.setFocusable(false);
		this.add(foreGround);
		foreGround.setFocusable(false);
		//this.add(bold);	
		
		popup = new ToolBarPopup(this);
		this.add(popup);
		this.setBorderPainted(true);
		this.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		this.setOpaque(true);
		this.setFloatable(false);
		this.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me){
				maybeShowPopup(me);
			}
		});		
		popupMenu = view.getPopup();
		this.setFloatable(false);
	}
	private void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}
	public void setScrollLockOperation(boolean flag){
		view.setScrollLock(flag);
		popupMenu.setScrollLockOperation(flag);
		updateButtons();
	}
	public void setEditableOperation(boolean flag){
		view.setViewEditable(flag);	
		popupMenu.setEditableOperation(flag);
		updateButtons();
		view.logView.requestFocusInWindow();
	}
	public void performCopyOperation(){
		view.performCopyOperation();
	}
	public void performCutOperation(){
		view.performCutOperation();
	}
	public void performPasteOperation(){
		view.performPasteOperation();
	}
	public void performDeleteOperation(){
		view.performDeleteOperation();
	}
	public void noAction(){
		JOptionPane.showMessageDialog(null, "No Action Defined Yet !!");
	}
	public void updateButtons(){
		if(UndoMan.getUndoMan().canUndo()){
			undo.setEnabled(true);
		} else {
			undo.setEnabled(false);
		}
		if(UndoMan.getUndoMan().canRedo()){
			redo.setEnabled(true);
		} else {
			redo.setEnabled(false);
		}
		if(popupMenu!=null)
		popupMenu.updateUndoRedo();
	}
	public void performUndoOperatiion(){
		view.performUndoOperation();
		updateButtons();
	}
	public void performRedoOperatiion(){
		view.performRedoOperation();
		updateButtons();
	}
	public void updateUI(){
		super.updateUI();
	}
}
