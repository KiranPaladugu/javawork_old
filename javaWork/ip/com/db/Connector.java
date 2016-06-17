package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {
	private Connection con;
	public Connector() {
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			// set this to a MS Access DB you have on your machine
			String filename = "IPFactor.mdb";
			String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
			database += filename.trim() + ";DriverID=22;READONLY=true}";
			// add on to the end
			// now we can get the connection from the DriverManager
			System.out.println("Connecting to " + filename.trim() + "...");
			con = DriverManager.getConnection(database, "", "");
			System.out.println("Connection Success");
			System.out.println("Closing Connection..");
			con.close();
			System.out.println("Closing Connection Success..");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	public Connection getConnection(){
		return con;
	}
}
