import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
public class ToolBarPopup extends JPopupMenu{
	private static final long serialVersionUID = -2139531182955146894L;
	JCheckBoxMenuItem enable ;
	JMenuItem coustomize;
	ToolBar toolbar;
	public ToolBarPopup(ToolBar toolbar) {
		this.toolbar = toolbar;
		init();
	}
	private void init(){
		enable = new JCheckBoxMenuItem("enable");		
		enable.setSelected(true);
		enable.addMouseListener(new MouseAdapter() {		
			public void mouseReleased(MouseEvent me){
				toolbar.setVisible(enable.isSelected());
			}
		});
		coustomize = new JMenuItem("coustomize..");
		this.add(coustomize);
		this.addSeparator();
		this.add(enable);
		
	}
	
}
