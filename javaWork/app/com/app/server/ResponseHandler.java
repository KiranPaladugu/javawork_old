package com.app.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

import com.app.http.HttpRequest;
import com.app.http.HttpResponse;
import com.app.http.ResponseContent;
import com.app.logservice.Logger;

public class ResponseHandler implements HttpResponse {
	private PrintWriter out;
	private Socket socket;

	public void sendRedirect(HttpRequest request, HttpResponse response) {
	}

	public void setContent(ResponseContent content) {
	}

	public void setRedirect() {
	}

	public void write(String content) {
	}

	public ResponseHandler(Socket socket) {
		this.socket = socket;
		try {
			this.out = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			Logger.log("Unable to get Output Stream for the client.");
			e.printStackTrace();
		}
		sendResponse();
	}

	@SuppressWarnings("deprecation")
	private void sendResponse() {
		String body = "<html>" + "<title>This is My Server....</title>\n"
				+ "<body bgcolor=wheat><center><h1> Hello how are u ????</h1>\n"
				+ "<h3> This page is under construction please try after some time...</h3>\n" + "<h4> and your ip is :"
				+ socket.getInetAddress() + "</h4>\n" + "<h4> Thanks for sending Request</h4></center>" + "</body></html>";
		String header = "HTTP/1.1 200 OK \n" + "Date:" + new Date().toGMTString() + "\nServer: servX(Windows/Linux/UNIX)\n"
				+ "Last-Modified: Wed, 08 Jan 2003 23:11:55 GMT\n" + "Etag: \"3f80f-1b6-3e1cb03b\"\n" + "Accept-Ranges: bytes\n"
				+ "Content-Length: " + body.length() + "\nConnection: close\n" + "Content-Type: text/html; charset=UTF-8";
		String content = header + "\n\n" + body;
		out.println(content);
		out.flush();
		out.close();
	}

}
