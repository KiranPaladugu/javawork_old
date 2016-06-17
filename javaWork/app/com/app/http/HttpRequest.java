package com.app.http;

public interface HttpRequest {
	/**
	 * This method gives requested url
	 * @return string representation of requested page .
	 */
	public String getRequestedURL();
	/**
	 * This method gives all the parameter names which present in request
	 * @return array of strings
	 */
	public String[] getParameterNames();
	/**
	 * This method returns list of parameternames
	 * @param parameterName
	 * @return -list of parameters as array of Strings
	 */
	public String getParameter(String parameterName);
	/**
	 * This method gives userAgent from with request is got.
	 * @return
	 */
	public String getUserAgent();
	/**
	 * This method returns Language from request
	 * @return 
	 */
	public String getLanguage();
	/**
	 * This method returns 
	 * @return
	 */
	public String getAcceptTypes();
	/**
	 * This method returns String representation of requested Query string
	 * @return
	 */
	public String getQueryString();
	/**
	 * return true if request is of type GET
	 * @return
	 */
	public boolean isGETRequest();
	/**
	 * retruns true if request is of type POST
	 * @return
	 */
	public boolean isPOSTRequest();
	/**
	 * This method returns String representation of request type
	 * @return
	 */
	public String getRequestType();
	
}
