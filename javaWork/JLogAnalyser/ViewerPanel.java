import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import reg.Register;

public class ViewerPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private ViewPanel view;
	private ResultView result;
	private static ToolBar toolBar;
	private static StatusBar statusBar;
	public ViewerPanel() {
		result = new ResultView();
		Register.register(ResultView.class, result);
		view = new ViewPanel();
		Register.register(ViewPanel.class, view);
		toolBar = new ToolBar(view);
		Register.register(ToolBar.class, toolBar);
		statusBar = new StatusBar();
		Register.register(StatusBar.class, statusBar);
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;		
		gc.gridwidth = GridBagConstraints.REMAINDER;
		gc.insets = new Insets(1, 1, 1, 1);
		
		gc.gridheight = 1;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 1;
		gc.weighty = 0;
		
		
		add(toolBar,gc);
		
		gc.gridheight = 30;
		gc.gridx = 0;
		gc.gridy = 1;
		gc.weightx = 1;
		gc.weighty = 1;

		
		add(view, gc);
		gc.gridx = 0;
		gc.gridy = 31;
		gc.gridheight = 20;
		gc.weightx = 1;
		gc.weighty = 0.15;
		
		add(result, gc);
		gc.gridx=0;
		gc.gridy=51;
		gc.gridheight=1;
		gc.weightx=1;
		gc.weighty=0;
		add(statusBar,gc);

	}
	
	public static ToolBar getToolbar(){
		return toolBar;
	}
	
	public ResultView getResult() {
		return result;
	}

	public ViewPanel getView() {
		return view;
	}	
	public static StatusBar getStatusBar(){
		return statusBar;
	}
}
