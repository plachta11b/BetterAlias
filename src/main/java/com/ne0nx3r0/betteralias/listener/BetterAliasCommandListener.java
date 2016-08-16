package com.ne0nx3r0.betteralias.listener;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import com.ne0nx3r0.betteralias.BetterAlias;
import com.ne0nx3r0.betteralias.alias.Alias;

public class BetterAliasCommandListener implements Listener {
    private final BetterAlias plugin;

    public BetterAliasCommandListener(BetterAlias plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerCommandPreprocessLowest(PlayerCommandPreprocessEvent e) {
    	onPlayerCommandPreprocess(e, EventPriority.LOWEST);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerCommandPreprocessLow(PlayerCommandPreprocessEvent e) {
    	onPlayerCommandPreprocess(e, EventPriority.LOW);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerCommandPreprocessNormal(PlayerCommandPreprocessEvent e) {
    	onPlayerCommandPreprocess(e, EventPriority.NORMAL);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerCommandPreprocessHigh(PlayerCommandPreprocessEvent e) {
    	onPlayerCommandPreprocess(e, EventPriority.HIGH);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerCommandPreprocessHighest(PlayerCommandPreprocessEvent e) {
    	onPlayerCommandPreprocess(e, EventPriority.HIGHEST);
    }

    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e, EventPriority priority) {

        String sCommand = e.getMessage().substring(1);

        if (this.plugin.config.isDebuggingAllowed() == true) {
        	this.plugin.getLogger().log(Level.INFO, "Player: " + e.getPlayer() + " issue command " + sCommand);
        }

        for (Alias alias : plugin.aliasManager.getAliasMatches(sCommand, priority)) {
            String sArgs = sCommand.substring(alias.command.length());

            Player player = e.getPlayer();
            String sNode = "betteralias." + alias.getPermissionNode();

            if (alias.hasPermission() && !player.hasPermission(sNode)) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use this alias.");
                player.sendMessage(ChatColor.GRAY + "Node: " + sNode);

                e.setCancelled(true);
            } else {
                if (plugin.aliasManager.sendAliasCommands(alias, (CommandSender) e.getPlayer(), sArgs)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onConsoleCommandLowest(ServerCommandEvent e) {
    	onConsoleCommand(e, EventPriority.LOWEST);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onConsoleCommandLow(ServerCommandEvent e) {
    	onConsoleCommand(e, EventPriority.LOW);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onConsoleCommandNormal(ServerCommandEvent e) {
    	onConsoleCommand(e, EventPriority.NORMAL);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onConsoleCommandHigh(ServerCommandEvent e) {
    	onConsoleCommand(e, EventPriority.HIGH);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onConsoleCommandHighest(ServerCommandEvent e) {
    	onConsoleCommand(e, EventPriority.HIGHEST);
    }

    public void onConsoleCommand(ServerCommandEvent e, EventPriority priority) {

        String sCommand = e.getCommand();

        if (this.plugin.config.isDebuggingAllowed() == true) {
        	this.plugin.getLogger().log(Level.INFO, "Server console: " + e.getSender() + " issue command " + sCommand);
        }

        for (Alias alias : plugin.aliasManager.getAliasMatches(sCommand, priority)) {
            String sArgs = sCommand.substring(alias.command.length());

            if (plugin.aliasManager.sendAliasCommands(alias, e.getSender(), sArgs)) {
            	// 
                e.setCommand("bareload donothing");
            }
        }
    }

    // Thanks to @DrOverbuild for this function
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void redstoneChanges(BlockRedstoneEvent e) {

    	if (this.plugin.config.isCommandBlockAllowed() == false) return;

    	Block block = e.getBlock();

    	if (e.getOldCurrent() == 0 && e.getNewCurrent() > 0) {
    	    if (block != null) {
    	        BlockState state = block.getState();
    	        if(state != null) {
    	            if (state instanceof CommandBlock) {
    	                CommandBlock cb = (CommandBlock)state;

    	                String sCommand = cb.getCommand();
    	                String sName = cb.getName();

    	                if (this.plugin.config.isDebuggingAllowed() == true) {
    	                    this.plugin.getLogger().log(Level.INFO, "CommandBlock: " + sName + " issue command " + sCommand);
    	                }

    	                for (Alias alias : plugin.aliasManager.getAliasMatches(sCommand, null)) {
    	                    String sArgs = sCommand.substring(alias.command.length());

    	                    // get location from block where commandblock is activated
    	                    int posX = block.getX();
    	                    int posY = block.getY();
    	                    int posZ = block.getZ();

    	                    // set execution location /execute @e[type=Player] x y z
    	                    String prefix = "execute @e[type=Player] " + posX + " " + posY + " " + posZ + " ";

    	                    ConsoleCommandSender sender = this.plugin.getServer().getConsoleSender();

    	                    plugin.aliasManager.sendAliasCommands(alias, sender, sArgs, prefix);
    	                }
    	            }
    	        }
    	    }
    	}
    }
}
