package com.lamesa.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread {

	private final Socket s;
	
	private final BufferedReader br;
	private final PrintWriter pw;
	
	public Client(Socket s) throws IOException {
		
		this.s = s;
		
		this.br = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
		this.pw = new PrintWriter(this.s.getOutputStream());
		
	}
	
	@Override
	public void run() {
		
		
		
	}
	
}
