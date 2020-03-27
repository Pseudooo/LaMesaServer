package com.lamesa.net;

import java.io.IOException;
import java.util.concurrent.ScheduledFuture;

import com.lamesa.util.TextFormat;

/**
 * Class to timeout clients should they not respond within a time-window
 * @author Mitchell
 *
 */
class TimeoutClient implements Runnable {
	
	private final Client c;
	
	/**
	 * Create a new event to time a client out
	 * @param c client
	 */
	public TimeoutClient(Client c) {
		this.c = c;
	}
	
	/**
	 * Runnable for a scheduled event
	 */
	@Override
	public void run() {
		try {
			this.c.getSocket().close();
		} catch (IOException e) {
			TextFormat.output("Failed to close socket");
		}
		
		this.c.interrupt();
		String host = this.c.getSocket().getInetAddress().getCanonicalHostName();
		TextFormat.foutput("%s has been timed out!", host);
	}
	
}
