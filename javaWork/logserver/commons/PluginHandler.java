package commons;

import java.util.List;

public interface PluginHandler {
	public String getName();
	public String getProfileName();
	public Object getProfile();
	public void addPlugin(Plugin plugin);
	public boolean removePlugin(Plugin plugin);
	public List<Plugin> getAll();
	public Plugin getPlugin(Class<Plugin> className);
	public boolean removeAll();
}
