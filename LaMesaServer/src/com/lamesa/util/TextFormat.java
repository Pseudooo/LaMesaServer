package com.lamesa.util;

import java.util.Date;

import com.lamesa.net.ClientHandler;

public class TextFormat {
	
	/**
	 * Format a key into 32 2-digit hex segments
	 * @param key
	 * @return string representation
	 */
	public static String formatKey(byte[] key) {
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < ClientHandler.KEY_SIZE - 1; i++) {
			sb.append(String.format("%02X:", key[i]));
		}
		sb.append(String.format("%02X", key[ClientHandler.KEY_SIZE - 1]));
		
		return sb.toString();
	}
	
	public static void output(String msg) {
		System.out.printf("[%tT] : %s%n", new Date(), msg);
	}
	
	public static void foutput(String mask, Object... params) {
		output(String.format(mask, params));
	}
	
}
