package com.ne0nx3r0.betteralias;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import com.ne0nx3r0.betteralias.alias.AliasManager;
import com.ne0nx3r0.betteralias.command.BetterAliasCommandExecutor;
import com.ne0nx3r0.betteralias.config.PluginConfig;
import com.ne0nx3r0.betteralias.listener.BetterAliasCommandListener;

public class BetterAlias extends JavaPlugin
{
    public AliasManager aliasManager;
    public PluginConfig config;

    @Override
    public void onEnable()
    {
        this.config = new PluginConfig(this);

        if (this.config.getDebuggingState() == true) {
        	this.getLogger().log(Level.INFO, "Debugging enabled by config");
        }

        this.aliasManager = new AliasManager(this);

        this.getServer().getPluginManager().registerEvents(new BetterAliasCommandListener(this), this);

        BetterAliasCommandExecutor betterAliasCommandExecutor = new BetterAliasCommandExecutor(this);

        this.getCommand("bareload").setExecutor(betterAliasCommandExecutor);
    }
    
    public boolean reload() {
    	
    	if (aliasManager.loadAliases() && this.config.reload()) {
    		return true;
    	}

    	return false;
    }
}
