package com.lamesa;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.UUID;

public class Server {
	
	// Handle entry point of the application
	public static void main(String[] args) {
		// TODO Instantiate server
	}

	private final int port;
	
	/**
	 * Instantiate a new server instance
	 * @param port to listen on
	 */
	public Server(int port) {
		this.port = port;
	}
	
	/**
	 * Start listening on port
	 * BLOCKS
	 */
	public void listen() {
		
		// Open socket on given port
		try(ServerSocket ss = new ServerSocket(this.port)) {
			System.out.printf("Listening on %d...%n", this.port);
			
			while(true) { // Listen for new connections perpetually
				// Accept new connection
				Socket s = ss.accept();
				
				// TODO Handle connection
				
			}
			
		}catch(Exception e) {
			System.err.println("Fatal Error!");
			e.printStackTrace();
		}
		
	}
	
	
	
}
