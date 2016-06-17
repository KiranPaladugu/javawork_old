package com.tcs.tmp;

public class PortListener {
	private int cmdPort = 7767;
	private int alarm = 7768;
	private int pipe = 7769;
	
	public PortListener() {
		new ServerSocketHandler(cmdPort,"CommandPort");
		new ServerSocketHandler(alarm, "AlarmPort");
		new ServerSocketHandler(pipe, "PipePort");
	}
	public static void main(String args[]) {
		new PortListener();
	}
}
