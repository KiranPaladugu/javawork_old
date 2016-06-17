package plugincore;

import commons.PluginContainer;
import commons.PluginHandler;

public class PluginContainerImpl implements PluginContainer {
	private String name=null;
	private PluginHandler pluginHandler= null;
	private PluginContainerImpl(){
		pluginHandler = new PluginHandlerImpl();
		name ="container";
	}
	public static PluginContainer getInstance(){
		return new PluginContainerImpl();
	}
	public PluginContainer getContainer() {		
		return this;
	}
	public PluginHandler getHandler() {		
		return pluginHandler;
	}
	public String getName() {		
		return name;
	}
	public String setName(String name) { 
		this.name = name;
		return name;
	}
}
