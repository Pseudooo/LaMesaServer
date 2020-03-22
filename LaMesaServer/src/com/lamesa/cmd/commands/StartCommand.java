package com.lamesa.cmd.commands;

import com.lamesa.cmd.CommandExecutor;
import com.lamesa.net.ClientHandler;
import com.lamesa.util.TextFormat;

public class StartCommand implements CommandExecutor {
	
	private final ClientHandler ch;
	
	public StartCommand(ClientHandler ch) {
		this.ch = ch;
	}
	
	@Override
	public void exec(String[] args) {
		
		if(this.ch.isListening()) {
			TextFormat.output("Server is already listening!");
		}else {
			this.ch.setListen(true);
			this.ch.start();
		}
		
	}
	
}
