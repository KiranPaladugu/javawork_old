package commons;

public interface Plugin {
	public String getName();
	public boolean setTaget(TargetHolder target);
	public boolean register();
	public boolean deRegister();
	public void addPluginListener(PluginListener listener);
}
