package com.app.reminder.obj;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.app.reminder.model.Container;
import com.app.reminder.model.ContainerEvent;
import com.app.reminder.model.ContainerListener;
import com.app.reminder.model.Context;


public class ApplicationContainer implements Container{

	private String name;
	private Context context;
	private Map<String,Container> containers = new HashMap<String, Container>();  
	private Vector<ContainerListener> listeners = new Vector<ContainerListener>();
	
	public void start() {
		start(1);
	}

	public void start(int code) {
		notifyListener(ContainerEvent.CONTAINER_START, this, "ContainersStart");
	}

	public Context getContext(String name) {
		return context;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addContainerListener(ContainerListener listener) {
		listeners.add(listener);
		notifyListener(ContainerEvent.CONTAINER_CHANGED, this, "ContainerChanged");
	}

	public void addContext(Context context) {
		this.context = context;
		notifyListener(ContainerEvent.CONTAINER_CHANGED, this, "ContextChanged");
	}

	public void destroy() {
		destroy(1);
	}

	public void destroy(int code) {
		notifyListener(ContainerEvent.CONTAINER_DESTROY, this, "ContainersDestroy");
	}

	public void add(String key,Container container) {
		containers.put(key,container);
		notifyListener(ContainerEvent.CONTAINER_ADDED, this, "ContainerAdded");
	}
	
	public Container getContainer(String name){
		return containers.get(name);
	}

	public void removeContainer(String name) {
		containers.remove(name);
		notifyListener(ContainerEvent.CONTAINER_REMOVED, this, "ContainerRemoved");
	}
	
	public void clearContainers(){
		containers.clear();
		notifyListener(ContainerEvent.CONTAINER_REMOVED, this, "ContainersCleared");
	}
	
	public void removeListener(ContainerListener listener){
		listeners.remove(listener);
		notifyListener(ContainerEvent.CONTAINER_CHANGED, this, "ListenerRemoved");
	}
	
	public void removeAllListeners(){
		listeners.clear();
		notifyListener(ContainerEvent.CONTAINER_CHANGED, this, "ListenersCleared");
	}
	

	private void notifyListener(int event, Object source, String name) {
		ContainerEvent ce = new ContainerEventImp(event, source, name,null);
		for (ContainerListener list : listeners) {
			list.containerChanged(ce);
		}
	}

	
}
