package com.lamesa.net;

import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.lamesa.util.TextFormat;

public class ClientHandler extends Thread {
	
	// new global key-size in case changes needed
	public static final int KEY_SIZE = 32;
	
	public final byte[] P, G;
	private final byte[] sec_key; // Secret key
	
	private volatile boolean listen = true;
	
	private final int port;
	
	private final ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
	
	private MessageDigest md;
	
	public ClientHandler(int port) {
		this.port = port;
		
		SecureRandom sr = new SecureRandom();
		byte[] buffer = new byte[KEY_SIZE];
		
		TextFormat.output("Generating Session Keys...");
		
		// Take all keys from a securely random pool
		sr.nextBytes(buffer);
		this.P = AbsAndPad(buffer);
		TextFormat.foutput("P = %s", TextFormat.formatKey(this.P));
		
		sr.nextBytes(buffer);
		this.G = AbsAndPad(buffer);
		TextFormat.foutput("G = %s", TextFormat.formatKey(this.G));
		
		sr.nextBytes(buffer);
		this.sec_key = AbsAndPad(buffer);
		TextFormat.foutput("S = %s", TextFormat.formatKey(this.sec_key));
		
		try {
			this.md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			TextFormat.output("Failed to collect SHA-256 instance");
			e.printStackTrace();
		}
		
	}
	
	private byte[] AbsAndPad(byte[] buffer) { // Helper func for above
		return Arrays.copyOf(new BigInteger(buffer).abs().toByteArray(), KEY_SIZE);
	}
	
	@Override
	public void run() {
		
		// Attempt to open on socket
		try(ServerSocket ss = new ServerSocket(this.port)) {
			
			TextFormat.foutput("Now accepting connections on port %d", this.port);
			
			while(this.listen) {
				
				Socket s = ss.accept();
				
				// Parallelise client's execution (avoid blocking listen)
				Client c = new Client(this, s);
				
				// After thread has been started notify console
				String host = s.getInetAddress().getCanonicalHostName();
				TextFormat.foutput("%s has started connecting", host);
				
				c.start();
				
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public byte[] getP() {
		return this.P;
	}
	
	public byte[] getG() {
		return this.G;
	}
	
	protected byte[] getSecretKey() {
		return this.sec_key;
	}
	
	/**
	 * Hash an array of bytes
	 * @param bytes
	 * @return hash
	 */
	protected byte[] digest(byte[] bytes) {
		return this.md.digest(bytes);
	}
	
	protected ScheduledExecutorService getSes() {
		return this.ses;
	}
	
}
