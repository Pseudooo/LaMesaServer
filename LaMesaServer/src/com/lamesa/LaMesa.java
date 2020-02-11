package com.lamesa;

import com.lamesa.net.ClientHandler;
import com.lamesa.util.TextFormat;

public class LaMesa {
	
	public static void main(String[] args) {
		
		// Validate Arguments provided
		if(args.length != 1) {
			System.err.println("Please provide a port!");
			return;
		}
		
		int port = 0;
		try {
			port = Integer.parseInt(args[0]);
		}catch(NumberFormatException e) {
			System.err.println("Port provided is not a number!");
			return;
		}
		
		TextFormat.output("Server Starting");
		
		// TODO Setup command interface
		
		// Setup listener
		ClientHandler ch = new ClientHandler(port);
		ch.start();
		
	}
	
}
