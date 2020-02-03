package com.lamesa.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class ClientHandler extends Thread {

	private final UUID id;
	
	private final Server server;
	private final Socket socket;
	
	private final PrintWriter pw;
	private final BufferedReader br;
	
	/**
	 * Create a new client-thread
	 * @param id
	 * @param server
	 * @param socket
	 * @throws IOException
	 */
	public ClientHandler(UUID id, Server server, Socket socket) throws IOException {
		
		this.id = id;
		this.server = server;
		this.socket = socket;
		
		// TODO: Consider auto-flush=false
		this.pw = new PrintWriter(this.socket.getOutputStream(), true);
		this.br = new BufferedReader(
				new InputStreamReader(this.socket.getInputStream()));
		
	}
	
	@Override
	public void run() {
		
		// Begin attempting of reading
		try {
			
			String msg;
			while(!(msg = br.readLine()).equals("terminate")) {
				System.out.printf("Recieved: %s%n", msg);
				System.out.printf("    from - %s%n", this.id.toString());
			}
			
			// Terminate method closes socket
			this.server.terminateSession(this.id);
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Call for this ClientHandler's UUID
	 * @return
	 */
	public UUID getUUID() {
		return this.id;
	}
	
	/**
	 * Call for this ClientHandler's Socket
	 * @return
	 */
	public Socket getSocket() {
		return this.socket;
	}
	
}
