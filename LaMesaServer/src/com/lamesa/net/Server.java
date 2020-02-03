package com.lamesa.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.UUID;

public class Server {

	private final int port;
	private final Hashtable<UUID, ClientHandler> sessions = new Hashtable<UUID, ClientHandler>();
	
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
				ClientHandler ct = new ClientHandler(UUID.randomUUID(), this, s);
				this.sessions.put(ct.getUUID(), ct);
				ct.start();
				
			}
			
		}catch(Exception e) {
			System.err.println("Fatal Error!");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Terminate an ongoing session
	 * @param id of session
	 */
	public void terminateSession(UUID id) throws IOException {
		if(!this.sessions.containsKey(id))
			return;
		
		this.sessions.get(id).getSocket().close();
		this.sessions.remove(id);
	}
	
}
