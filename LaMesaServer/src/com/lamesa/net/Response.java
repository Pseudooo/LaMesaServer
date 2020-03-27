package com.lamesa.net;

public interface Response {
	
	/**
	 * Method to accept a payload when it's received
	 * @param obj
	 */
	void takePayload(Object obj);
	
	/**
	 * Runnable to execute the response
	 */
	void run();
}
