package commons.pluign;

import commons.com.utils.TargetHolder;

public interface Plugin {
	public String getName();
	public boolean setTaget(TargetHolder target);
	public boolean register();
	public boolean deRegister();
}
