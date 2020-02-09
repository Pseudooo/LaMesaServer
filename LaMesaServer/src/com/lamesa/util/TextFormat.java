package com.lamesa.util;

import java.util.Date;

public class TextFormat {

	public static String formatKey(byte[] key) {
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < 31; i++) {
			sb.append(String.format("%02X:", key[i]));
		}
		sb.append(String.format("%02X", key[31]));
		
		return sb.toString();
	}
	
	public static void output(String msg) {
		System.out.printf("[%tT] : %s%n", new Date(), msg);
	}
	
}
