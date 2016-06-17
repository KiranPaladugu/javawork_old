package commons.pluign;

import commons.com.utils.TargetHolder;

public interface PluginAction {
	public String getName();
	public void setName(String name);
	public void setTarget(TargetHolder target);
	public boolean performAction();
}
