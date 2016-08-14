package com.ne0nx3r0.betteralias.config;

import java.io.File;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.ne0nx3r0.betteralias.BetterAlias;

public class PluginConfig {

	private static Configuration configuration;

	private final File configurationFile;

	public PluginConfig(final BetterAlias plugin) {

		plugin.saveDefaultConfig();
		plugin.saveConfig();

		PluginConfig.configuration = plugin.getConfig();
		configurationFile = new File(plugin.getDataFolder(), "config.yml");
	}

	public boolean reload() {
		PluginConfig.configuration = YamlConfiguration.loadConfiguration(configurationFile);
		
		return !PluginConfig.configuration.getKeys(false).isEmpty();
	}

	public boolean getDebuggingState() {
		return PluginConfig.configuration.getBoolean("debug");
	}
}
