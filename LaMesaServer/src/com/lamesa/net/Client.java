package com.lamesa.net;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import com.lamesa.net.exceptions.HandshakeFailedException;
import com.lamesa.util.TextFormat;

public class Client extends Thread {
	
	private byte[] key = new byte[ClientHandler.KEY_SIZE];
	
	private final UUID id;
	
	private final ClientHandler handler;
	
	private final Socket s;
	
	protected Client(ClientHandler handler, Socket s) throws IOException {
		
		this.id = UUID.randomUUID();
		
		this.handler = handler;
		
		this.s = s;
		
	}
	
	@Override
	public void run() {
		
		// Initialise connection
		init();
		
	}
	
	private void init() {
		
		// Schedule timeout
		ScheduledFuture<?> future = this.handler.getSes().schedule(
				new TimeoutClient(this), 1, TimeUnit.MINUTES);
		boolean success = true;
		
		try {
			// Attempt a handshake
			performHandshake();
		}catch(HandshakeFailedException e) {
			
			// Handshake has failed
			success = false;
			TextFormat.foutput("%s has failed their handshake", 
					this.s.getInetAddress().getCanonicalHostName());
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
			TextFormat.foutput("%s is now connected",
					this.s.getInetAddress().getCanonicalHostName());
			this.handler.registerClient(this);
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
	
	public UUID getID() {
		return this.id;
	}
	
	protected Socket getSocket() {
		return this.s;
	}
	
}
