package com.lamesa.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler extends Thread {
	
	private volatile boolean listen = true;
	
	private final int port;
	
	public ClientHandler(int port) {
		this.port = port;
	}
	
	@Override
	public void run() {
		
		try(ServerSocket ss = new ServerSocket(this.port)) {
			
			while(listen) {
				
				Socket s = ss.accept();
				
				// TODO Handle client
				
			}
			
		}catch(IOException e) {
			
		}
		
	}
	
}
