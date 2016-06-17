package com.app.server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.Socket;
import java.util.Map;
import java.util.Vector;

import com.app.http.HttpRequest;
import com.app.logservice.Logger;

public class RequestHandler extends Thread implements HttpRequest{
	private Socket socket;
	private String requestString="";
	private Map<String,String> parameters;
	private String userAgent;
	private String language;
	private String acceptTypes;
	private String queryString;
	private String requestUrl;
	private String requestMethod;
	private Vector<String> req;
	public RequestHandler(Socket client) {
		Logger.log("got request from client:"+client.getInetAddress());
		this.socket=client;
		this.start();
	}
	public void run(){
		try {
			int reqend=0;
			StringBuffer strBuf= new StringBuffer();
			Logger.log("processing input from client:"+socket.getInetAddress());
			InputStream inputStream = socket.getInputStream();					
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String str = "";
			while(((str=br.readLine())!=null)&&(reqend<=2)){
				if(str.equalsIgnoreCase("")){
					reqend++;					
				}
				strBuf.append(str+"\n");
			}
			requestString = strBuf.toString();
			getRequest();
			System.out.println("request Details:\n"+requestString);
			new ResponseHandler(socket);
			inputStream.close();
			Logger.log("response has been sent to client...");
			Logger.log("closing connection from client....");			
			Logger.log("Connection is terminated....");
			if(!socket.isClosed()){
				socket.close();
			}
			socket =null;
		} catch (IOException e) {
			Logger.log("Unable to get SocketInputstream now Exiting for request...:"+socket.getInetAddress());
		}
	}
	public String getRequestString(){
		return requestString;
	}
	public String getRequestedURL(){
		return requestUrl;
	}
	private void getRequest(){
		req = new Vector<String>();
		int lines=0;
		String str = "";
		ByteArrayInputStream bais = new ByteArrayInputStream(requestString.getBytes());
		LineNumberReader lineReader = new LineNumberReader(new InputStreamReader(bais));
		try {
			while((str = lineReader.readLine())!=null){
				if (!str.equalsIgnoreCase("")) {
					req.add(str);
				}
			}
		} catch (IOException e) {
			Logger.log("Unable to get requestedURL...");
			e.printStackTrace();
		}
		if(req.size()<1){
			// not a valid request
		}else {
			if(processRequestHeader(req.get(0))){
				lines = req.size();
				for(int i=1;i<lines;i++){
					processRequest(req.get(i));
				}
			}
		}
	}
	private void processRequest(String str){
		
	}

	private boolean processRequestHeader(String str) {
		String s[] = str.trim().split(" ");
		if ((s.length == 3) && s[s.length - 1].contains("HTTP/")) {
			requestMethod = s[0];
			requestUrl = s[1];
			return true;
		}else
		return false;
	}
	public String getAcceptTypes() { 
		return acceptTypes;
	}
	public String getLanguage() {
		return language;
	}
	public String getParameter(String parameterName) {
		return parameters.get(parameterName);
	}
	public String[] getParameterNames() {
		return (String[])parameters.keySet().toArray();
	}
	public String getQueryString() {
		return queryString;
	}
	public String getRequestType() {
		return null;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public boolean isGETRequest() {
		return false;
	}
	public boolean isPOSTRequest() {
		return false;
	}
	public String getRequestMethod(){
		return requestMethod;
	}
}
