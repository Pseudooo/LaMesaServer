package com.lamesa.cmd;

import java.util.Hashtable;

public class CMDHandler {

	private final Hashtable<String, CommandExecutor> cmds;
	
	public CMDHandler() {
		this.cmds = new Hashtable<String, CommandExecutor>();
	}
	
	// Register a command
	public void registerCmd(String command, CommandExecutor executor) {
		this.cmds.put(command, executor);
	}
	
	// Check if a command is registered
	public boolean isRegistered(String cmd) {
		return this.cmds.keySet().contains(cmd);
	}
	
}
