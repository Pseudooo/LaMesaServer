package com.lamesa.net;

import java.io.IOException;
import java.util.concurrent.ScheduledFuture;

import com.lamesa.util.TextFormat;

class TimeoutClient implements Runnable {
	
	private final Client c;
	
	public TimeoutClient(Client c) {
		this.c = c;
	}
	
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
