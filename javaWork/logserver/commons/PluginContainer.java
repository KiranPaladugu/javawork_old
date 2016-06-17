package commons;

public interface PluginContainer {
	public PluginContainer getContainer();
	public PluginHandler getHandler();
	public String getName();
	public String setName(String name);
}
