package com.lamesa.net;

import java.util.concurrent.ScheduledFuture;

import com.lamesa.util.TextFormat;

class TimeoutClient implements Runnable {

	private final Client c;
	private ScheduledFuture<?> sf;
	
	public TimeoutClient(Client c) {
		this.c = c;
	}
	
	@Override
	public void run() {
		this.c.interrupt();
		String host = this.c.getSocket().getInetAddress().getCanonicalHostName();
		TextFormat.foutput("%s has been timed out!", host);
	}
	
}
