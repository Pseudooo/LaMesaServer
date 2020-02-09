package com.lamesa.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.lamesa.net.exceptions.HandshakeFailedException;
import com.lamesa.util.TextFormat;

public class Client extends Thread {
	
	private byte[] key = new byte[32];
	
	private final ClientHandler handler;
	
	private final Socket s;
	
	private final BufferedReader br;
	private final PrintWriter pw;
	
	public Client(ClientHandler handler, Socket s) throws IOException {
		
		this.handler = handler;
		
		this.s = s;
		
		this.br = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
		this.pw = new PrintWriter(this.s.getOutputStream());
		
	}
	
	@Override
	public void run() {
		
		// TODO Schedule timeout to interrupt `this` thread (timeout)
		
		try {
			performHandshake();
		}catch(HandshakeFailedException e) {
			// TODO Handle exception
		}
		
	}
	
	/**
	 * Performs a handshake meaning that a secret key is obtained
	 * by both server and client without ever being broadcast
	 * @throws HandshakeFailedException
	 */
	private void performHandshake() throws HandshakeFailedException {
		
		try { // Send public keys
			this.s.getOutputStream().write(this.handler.getP());
			this.s.getOutputStream().write(this.handler.getG());
		} catch (IOException e) {
			throw new HandshakeFailedException("Failed to write public keys");
		}
		
		// Initialize x & y
		// Using copy to pad out 0s and maintain 32 bits
		byte[] x = Arrays.copyOf(new BigInteger(this.handler.getG()).modPow(
				new BigInteger(this.handler.getSecretKey()),
				new BigInteger(this.handler.getP())).toByteArray(), 32);
		
		byte[] y = new byte[32];
		
		try { // Send x + request y
			this.s.getOutputStream().write(x);
			this.s.getInputStream().read(y);
		}catch(IOException e) {
			throw new HandshakeFailedException("Failed to exchange x and y");
		}
		
		byte[] secret = Arrays.copyOf(new BigInteger(y).modPow(
				new BigInteger(this.handler.getSecretKey()), 
				new BigInteger(this.handler.getP())).toByteArray(), 32);
		
		this.key = this.handler.digest(secret);
		
		// Display connection results
		TextFormat.output(String.format("%s has established the following key", this.s.getInetAddress().getCanonicalHostName()));
		TextFormat.output(TextFormat.formatKey(this.key));
		
	}
	
}
