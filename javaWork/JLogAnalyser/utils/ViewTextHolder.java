package utils;

import java.nio.CharBuffer;

import javax.swing.JTextArea;

public class ViewTextHolder {
	private CharBuffer buffer;

	public ViewTextHolder() {
		buffer = CharBuffer.allocate(10485760);
	}

	public ViewTextHolder(int size) {
		buffer = CharBuffer.allocate(size);
	}
	public CharBuffer getBuffer(){
		return buffer;
	}
	public void addString(String str) {
		JTextArea view = new JTextArea();
		view.append(str);		
	}

}
