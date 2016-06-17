import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;


public class ViewKeyListener implements KeyListener{
	ViewPanel view;
	public ViewKeyListener(ViewPanel viewPanel) {
		this.view=viewPanel;
	}
	public void keyPressed(KeyEvent e) {		
		if(e.isControlDown()){		
			if(e.getKeyCode()==KeyEvent.VK_DELETE){
				view.performClearOperation();
			}
			if(e.getKeyCode()==KeyEvent.VK_L){
				view.setScrollLock(true);
			}
			if(e.getKeyCode() == KeyEvent.VK_F){
				noOpMsg();
			}
			if(e.getKeyCode() == KeyEvent.VK_Z){
				view.performUndoOperation();
				ViewerPanel.getToolbar().updateButtons();
			}
			if(e.getKeyCode() == KeyEvent.VK_Y){
				view.performRedoOperation();
				ViewerPanel.getToolbar().updateButtons();
			}
		}	
	}

	private void noOpMsg() {
		JOptionPane.showMessageDialog(null, "No Operation Defined !!");
	}
	public void keyReleased(KeyEvent e) {
		
	}

	public void keyTyped(KeyEvent e) {
		
	}

}
