package plugincore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import commons.Plugin;
import commons.PluginHandler;

public class PluginHandlerImpl implements PluginHandler {
	private String name = null;
	private Map<Class<?>, Plugin> map=null;
	public PluginHandlerImpl(){
		map = new HashMap<Class<?>, Plugin>();
		name ="Handler";
	}
	public void addPlugin(Plugin plugin) {
		map.put(plugin.getClass(), plugin);		
	}

	public List<Plugin> getAll() {		
		return null;
	}

	public String getName() {
		return name;
	}

	public Plugin getPlugin(Class<Plugin> className) {
		return map.get(className);
	}

	public Object getProfile() {
		return null;
	}

	public String getProfileName() {
		return null;
	}

	public boolean removeAll() {
		map.clear();
		return true;
	}

	public boolean removePlugin(Plugin plugin) {
		map.remove(plugin.getClass());
		return false;
	}

}
