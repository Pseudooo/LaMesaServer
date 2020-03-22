package com.lamesa.net.exceptions;

public class HandshakeFailedException extends Exception {
	private static final long serialVersionUID = -1006681819194090147L;

	public HandshakeFailedException(String msg) {
		super(msg);
	}
}
