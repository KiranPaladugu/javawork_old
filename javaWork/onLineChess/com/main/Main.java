package com.main;

import com.reg.Register;
import com.view.MainFrame;

public class Main {
	public static void main(String[] args) {
		Register.deRegisterAll();
		Register.register(MainFrame.class, new MainFrame());
	}
}
