package newapp;

public interface Context {
	public Object getObject(Class<?> className) throws RegisterException;
	public String getParameter(String name);
	public String[] getInitParamNames(Class<?> className);
	public String getInitParam(Class<?> className);
	public String[] getContextParamNames();
	public String getContextParam(String name);
	public Container getContainer();
}
