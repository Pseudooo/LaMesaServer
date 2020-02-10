package com.lamesa.net;

import com.lamesa.util.TextFormat;

class TimeoutClient implements Runnable {

	private final Client c;
	
	public TimeoutClient(Client c) {
		this.c = c;
	}
	
	@Override
	public void run() {
		this.c.interrupt();
		String host = this.c.getSocket().getInetAddress().getCanonicalHostName();
		TextFormat.output(String.format("%s has been timed out!", host));
	}
	
}
