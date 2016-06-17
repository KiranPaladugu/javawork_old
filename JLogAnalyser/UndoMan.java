import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;


public class UndoMan extends UndoManager  implements UndoableEditListener{
	private static final long serialVersionUID = -3784952024538203365L;
	private static UndoMan undoMan = new UndoMan();
	public static UndoMan getUndoMan(){
		return undoMan;
	}
	public void undoableEditHappened(UndoableEditEvent e) {				
		UndoMan.getUndoMan().addEdit(e.getEdit());
		ViewerPanel.getToolbar().updateButtons();
	}
}
