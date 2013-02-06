package com.ne0nx3r0.betteralias;

import com.ne0nx3r0.betteralias.alias.AliasManager;
import com.ne0nx3r0.betteralias.listener.BetterAliasPlayerListener;
import com.ne0nx3r0.betteralias.listener.command.BetterAliasCommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterAlias extends JavaPlugin
{
    public AliasManager aliasManager;
    
    @Override
    public void onEnable()
    {
        this.aliasManager = new AliasManager(this);
        
        this.getServer().getPluginManager().registerEvents(new BetterAliasPlayerListener(this), this);
        
        
        BetterAliasCommandExecutor betterAliasCommandExecutor = new BetterAliasCommandExecutor(this);
        
        this.getCommand("bareload").setExecutor(betterAliasCommandExecutor);
        
        //Filler command to null things to since console commands are not cancellable
        this.getCommand("badonothing").setExecutor(betterAliasCommandExecutor);
    }
}