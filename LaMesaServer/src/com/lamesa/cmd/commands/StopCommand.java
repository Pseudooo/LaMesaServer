package com.lamesa.cmd.commands;

import com.lamesa.cmd.CommandExecutor;
import com.lamesa.net.ClientHandler;
import com.lamesa.util.TextFormat;

public class StopCommand implements CommandExecutor {
	
	private final ClientHandler handler;
	
	public StopCommand(ClientHandler handler) {
		this.handler = handler;
	}
	
	@Override
	public void exec(String[] args) {
		
		if(this.handler.isListening()) {
			this.handler.setListen(false);
			this.handler.interrupt();
			TextFormat.output("Listening has been stopped!");
		}else {
			TextFormat.output("Sever is not currently listening!");
		}
		
	}
	
}
