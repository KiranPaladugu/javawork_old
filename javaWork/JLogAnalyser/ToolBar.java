import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.instrument.UnmodifiableClassException;
import java.nio.channels.NotYetBoundException;
import java.nio.channels.NotYetConnectedException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.text.Document;

import reg.Register;

public class ToolBar extends JToolBar{
	/**
	 * 
	 */
	private static final long serialVersionUID = -657894987268790763L;
	private JButton clear;
	private JButton undo,redo,delete,copy,cut,paste,backGround,foreGround;
	private JToggleButton sLock,editable,bold,resultView;
	private JButton reload;
	private ToolBarPopup popup;
	private ViewPanel view;
	private ResultView result;
	private PopupMenu popupMenu;
	private JButton next,stop;	
	private JToggleButton saveLog;
	private String filename=null;
	public ToolBar(ViewPanel view) {
		this.view = view;
		init();
		this.requestFocusInWindow();
	}
	private void init(){
		clear = new JButton();
		Image clear_image= Toolkit.getDefaultToolkit().getImage(ToolBar.class.getResource("clear.png"));
		ImageIcon clear_img= new ImageIcon(clear_image);
		clear.setIcon(clear_img);
		clear.setToolTipText("Clear");
		clear.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				Document doc = view.logView.getDocument();
				doc.removeUndoableEditListener(UndoMan.getUndoMan());
				UndoMan.getUndoMan().discardAllEdits();
				updateButtons();
				view.logView.getAccessibleContext().getAccessibleEditableText().setTextContents("");
				doc.addUndoableEditListener(UndoMan.getUndoMan());
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
		undo.setFocusable(false);
		undo.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ToolBar.class.getResource("undo.png"))));
		redo = new JButton();
		redo.setFocusable(false);
		redo.setEnabled(false);
		redo.setToolTipText("Redo");
		redo.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				performRedoOperatiion();
			}
		});
		redo.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ToolBar.class.getResource("redo.png"))));
		delete = new JButton();
		delete.setToolTipText("Delete");
		delete.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				performDeleteOperation();
			}
		});
		delete.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ToolBar.class.getResource("edit-delete.png"))));
		copy = new JButton();
		copy.setToolTipText("Copy");
		copy.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				performCopyOperation();
			}
		});
		copy.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ToolBar.class.getResource("copy.png"))));
		cut = new JButton();
		cut.setToolTipText("Cut");
		cut.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				performCutOperation();
			}
		});
		cut.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ToolBar.class.getResource("cut.png"))));
		paste = new JButton();
		paste.setToolTipText("Paste");
		paste.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				performPasteOperation();
			}
		});
		paste.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ToolBar.class.getResource("edit-paste.png"))));
		sLock = new JToggleButton();
		sLock.setToolTipText("Lock Scroll");
		sLock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setScrollLockOperation(((JToggleButton) e.getSource()).isSelected());
			}
		});
		sLock.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ToolBar.class.getResource("lock.png"))));
		editable = new JToggleButton();
		editable.setToolTipText("Set Editable");
		editable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setEditableOperation(((JToggleButton) e.getSource()).isSelected());
			}
		});
		editable.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ToolBar.class.getResource("edit.png"))));
		backGround = new JButton();
		backGround.setToolTipText("Back Ground Color");
		backGround.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(null, "Choose Back ground", Color.white);
				view.logView.setBackground(color);
			}
		});
		backGround.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ToolBar.class.getResource("background.png"))));
		foreGround = new JButton();
		foreGround.setToolTipText("Fore Ground Color");
		foreGround.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(null, "Choose Fore ground", Color.BLACK);
				view.logView.setForeground(color);
			}
		});
		foreGround.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ToolBar.class.getResource("color.png"))));
		
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
		
		bold.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ToolBar.class.getResource("bold.png"))));
		result = (ResultView) Register.getObject(ResultView.class);
		resultView = new JToggleButton();
		resultView.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ToolBar.class.getResource("result.png"))));
		resultView.setSelected(true);
		resultView.setFocusable(false);
		resultView.setToolTipText("Show Result View");
		resultView.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				if(result !=null ){
					JToggleButton button = (JToggleButton) e.getSource();
					result.setVisible(button.isSelected());
				}
			}
		});
		
		reload = new JButton();
		reload.setFocusable(false);
		reload.setToolTipText("Reload the file");
		reload.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ToolBar.class.getResource("Arrow_refresh.png"))));
		reload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuView menu = (MenuView)Register.getObject(MenuView.class);
				if(menu != null){
					String current = menu.getCurrentFile();
					if(current !=null ){
						view.logView.setText("");
						menu.openOperation(current);
					}
				}
			}
		});
		next = new JButton();
		next.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ToolBar.class.getResource("go-next.png"))));
		next.setToolTipText("Next...");
		next.setEnabled(false);
		next.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				next.setEnabled(false);				
				TailerThread.setPause(false);
				
			}
		});
		stop = new JButton();
		stop.setFocusable(false);
		stop.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ToolBar.class.getResource("stop-icon.png"))));
		stop.setToolTipText("Stop");
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TailerThread.stop();
			}
		});
		
		
		next.setFocusable(false);
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
		this.add(resultView);
		this.add(reload);
		add(next);
		add(stop);
//		add(saveLog);
		
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
	public boolean setPause(){
		TailerThread.setPause(true);		
		System.out.println("Called Pause..");
		next.setEnabled(true);		
		return true;
	}
	private void saveLogOption(boolean selected) {
		if(selected){
		checkForName();
		}else{
			filename = null;
		}
	}
	private void checkForName() {
		throw new NotYetBoundException();
	}
}
