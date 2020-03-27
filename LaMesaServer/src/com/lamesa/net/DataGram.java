package com.lamesa.net;

import java.io.Serializable;
import java.util.UUID;

class DataGram implements Serializable {
	
	private static final long serialVersionUID = 3219628280191397934L;

	private final UUID ID;
	
	private final Object payload;
		
	/**
	 * Create a new DataGram with a dynamically allocated UUID
	 * @param payload Data to be sent with the Datagram
	 */
	public DataGram(Object payload) {
		this.payload = payload;
		this.ID = UUID.randomUUID();
	}
	
	/**
	 * Create a new DataGram with an assigned UUID
	 * @param payload Data to be sent with the DataGram
	 * @param id Forced UUID of DataGram
	 */
	public DataGram(Object payload, UUID id) {
		this.ID = id;
		this.payload = payload;
	}
	
	/**
	 * Get the UUID of the DataGram
	 * @return UUID
	 */
	public UUID ID() {
		return this.ID;
	}
	
	/**
	 * Get the Payload of the DataGram
	 * @return Payload
	 */
	public Object getPayload() {
		return this.payload;
	}
	
}
