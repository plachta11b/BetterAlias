package com.ne0nx3r0.betteralias.listener;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
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

        for (Alias alias : plugin.aliasManager.getAliasMatches(sCommand, priority)) {
            String sArgs = sCommand.substring(alias.command.length());

            if (plugin.aliasManager.sendAliasCommands(alias, e.getSender(), sArgs)) {
                e.setCommand("bareload donothing");
            }
        }
    }
}
