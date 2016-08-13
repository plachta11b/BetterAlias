package com.ne0nx3r0.betteralias.alias;

import java.util.HashMap;
import java.util.List;

import org.bukkit.event.EventPriority;

public class Alias
{
    public final String command;
    public final boolean caseSensitive;
    private final String permission;
    private final EventPriority priority;
    private final HashMap<Integer, List<AliasCommand>> parameters;

    public Alias(String commandName, boolean caseSensitive, String permissionNode, String priority)
    {
        this.caseSensitive = caseSensitive;
        
        if(this.caseSensitive)
        {
            this.command = commandName;
        }
        else
        {
            this.command = commandName.toLowerCase();
        }

        if (priority != null) {
        	switch (priority.toLowerCase()) {
			case "lowest":
				this.priority = EventPriority.LOWEST;
				break;
			case "low":
				this.priority = EventPriority.LOW;
				break;
			case "normal":
				this.priority = EventPriority.NORMAL;
				break;
			case "high":
				this.priority = EventPriority.HIGH;
				break;
			case "highest":
				this.priority = EventPriority.HIGHEST;
				break;

			default:
				this.priority = EventPriority.LOWEST;
				break;
			}
        } else {
        	this.priority = EventPriority.LOWEST;
        }
        
        this.permission = permissionNode;
        
        this.parameters = new HashMap<Integer,List<AliasCommand>>();
    }

    public EventPriority getPriority() {
    	return this.priority;
    }
    
    public boolean hasCommandFor(int length)
    {
        return this.parameters.containsKey(length) || this.parameters.containsKey(-1);
    }

    public String getPermissionNode()
    {
        return this.permission;
    }

    public boolean hasPermission()
    {
        return this.permission != null;
    }

    Iterable<AliasCommand> getCommands(int length)
    {
        List<AliasCommand> commands = this.parameters.get(length);
        
        if(commands != null)
        {
            return commands;
        }

        return this.parameters.get(-1);
    }

    public void setCommandsFor(int length,List<AliasCommand> commandsList)
    {
        this.parameters.put(length, commandsList);
    }
}
