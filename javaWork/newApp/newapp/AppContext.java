package newapp;

public class AppContext implements Context{	
	private static AppContext context = new AppContext();
	private AppContext() {
	}
	public static Context getContext(){		
		return context;
	}
	public Object getObject(Class<?> className) throws RegisterException{
		return Register.getCheckedObject(className);
	}
	public String getParameter(String name){
		return null;
	}
	public Container getContainer() {		
		return null;
	}
	public String getContextParam(String name) {		
		return null;
	}
	public String[] getContextParams() {		
		return null;
	}
	public String getInitParam(Class<?> className) {		
		return null;
	}
	public String[] getInitParams(Class<?> className) {
		return null;
	}
	public String[] getContextParamNames() {		
		return null;
	}
	public String[] getInitParamNames(Class<?> className) {
		return null;
	}
}
