package com.samp;

import java.sql.*;

class Test {
	public static void main(String[] args) {
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			// set this to a MS Access DB you have on your machine
			String filename = "IPFactor.mdb";
			String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
			database += filename.trim() + ";DriverID=22;READONLY=true}"; 
			// add on to the end
			// now we can get the connection from the DriverManager
			System.out.println("Connecting to "+filename.trim()+"...");
			Connection con = DriverManager.getConnection(database, "", "");
			System.out.println("Connection Success");
			System.out.println("Closing Connection..");
			con.close();
			System.out.println("Closing Connection Success..");
			
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}
}