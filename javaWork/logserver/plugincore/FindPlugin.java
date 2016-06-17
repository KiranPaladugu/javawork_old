package plugincore;

import commons.Plugin;
import commons.PluginEvent;
import commons.PluginListener;
import commons.TargetHolder;

public class FindPlugin implements Plugin{
	private FindPluginListener listener;
	private String name;
	public void addPluginListener(FindPluginListener listener) {
		this.listener = listener;
	}
	public void doFindAction(Object... object){	
		PluginEvent event = new PluginEvent() {			
			public Object getSoruce() {
				return null;
			}
		};
		listener.eventHappened(event);
	}

	public boolean deRegister() {		
		return false;
	}

	public String getName() {
		return name;
	}
	public void setName(String name){
		this.name = name;
	}

	public boolean register() {
		return false;
	}

	public boolean setTaget(TargetHolder target) {
		return false;
	}

	public void addPluginListener(PluginListener listener) {
		if (listener instanceof FindPluginListener){
			addPluginListener(listener);
		}
	}

}
