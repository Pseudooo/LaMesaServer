package com.lamesa.cmd;

import java.util.Scanner;

import com.lamesa.cmd.commands.StartCommand;
import com.lamesa.cmd.commands.StopCommand;
import com.lamesa.net.ClientHandler;
import com.lamesa.util.TextFormat;

public class CommandIF extends Thread {

	private final CMDHandler cmdhandler;
	private final ClientHandler clientHandler;
	
	public CommandIF(ClientHandler clientHandler) {
		
		this.cmdhandler = new CMDHandler();
		this.clientHandler = clientHandler;
		
		// Register commands
		this.cmdhandler.registerCmd("stop", new StopCommand(this.clientHandler));
		this.cmdhandler.registerCmd("start", new StartCommand(this.clientHandler));
		
	}
	
	@Override
	public void run() {
		
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		boolean run = true;
		
		while(run) {
			
			String cmd = sc.nextLine().toLowerCase();
			
			if(!this.cmdhandler.isRegistered(cmd)) {
				// Invalid Command
				TextFormat.foutput("%s is not a registered command!", cmd);
				continue; // Back to beginning
			}
			
			// TODO Handle command
			
		}
		
	}
	
}


