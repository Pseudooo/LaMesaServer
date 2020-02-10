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
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import com.lamesa.net.exceptions.HandshakeFailedException;
import com.lamesa.util.TextFormat;

public class Client extends Thread {
	
	private byte[] key = new byte[ClientHandler.KEY_SIZE];
	
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
		
		// Schedule timeout
		ScheduledFuture<?> future = this.handler.getSes().schedule(new TimeoutClient(this), 1, TimeUnit.MINUTES);
		boolean success = true;
		
		try {
			// Attempt a handshake
			performHandshake();
		}catch(HandshakeFailedException e) {
			
			// Handshake has failed
			success = false;
			TextFormat.output(String.format("%s has failed their handshake", 
					this.s.getInetAddress().getCanonicalHostName()));
			TextFormat.output("   ~ "+e.getMessage());
			
			try { // Attempt to close the connection
				this.s.close();
			}catch(IOException e_) {
				TextFormat.output("   ~ Failed to close socket");
			}
			
		}
		
		// Cancel timeout
		future.cancel(true);
		
		if(success) {
			TextFormat.output(
					String.format("%s has finished connecting", this.s.getInetAddress().getCanonicalHostName()));
		}
	}
	
	/**
	 * Performs a handshake meaning that a secret key is obtained
	 * by both server and client without ever being broadcast
	 * @throws HandshakeFailedException
	 */
	private void performHandshake() throws HandshakeFailedException {
		
		// Abbreviate
		final int ks = ClientHandler.KEY_SIZE;
		
		try { // Send public keys
			this.s.getOutputStream().write(this.handler.getP());
			this.s.getOutputStream().write(this.handler.getG());
		} catch (IOException e) {
			throw new HandshakeFailedException("Failed to dispatch public keys");
		}
		
		// Initialise x & y
		// Using copy to pad out 0s and maintain `ks` bits
		byte[] x = Arrays.copyOf(new BigInteger(this.handler.getG()).modPow(
				new BigInteger(this.handler.getSecretKey()),
				new BigInteger(this.handler.getP())).toByteArray(), ks);
		
		byte[] y = new byte[ks];
		
		try { // Send x + request y
			this.s.getOutputStream().write(x);
			this.s.getInputStream().read(y);
		}catch(IOException e) {
			throw new HandshakeFailedException("Failed to exchange x and y");
		}
		
		byte[] secret = Arrays.copyOf(new BigInteger(y).modPow(
				new BigInteger(this.handler.getSecretKey()), 
				new BigInteger(this.handler.getP())).toByteArray(), ks);
		
		// Assign key to hash to shared secret
		this.key = this.handler.digest(secret);
		
	}
	
	protected Socket getSocket() {
		return this.s;
	}
	
}
