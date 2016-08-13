package com.ne0nx3r0.betteralias.config;

import java.io.File;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.ne0nx3r0.betteralias.BetterAlias;

public class PluginConfig {

	private Configuration configuration;

	private final File configurationFile;

	public PluginConfig(final BetterAlias plugin) {

		plugin.saveDefaultConfig();
		plugin.saveConfig();

		configuration = plugin.getConfig();
		configurationFile = new File(plugin.getDataFolder(), "config.yml");
	}

	// TODO Synch with bareload
	public void reload() {
		configuration = YamlConfiguration.loadConfiguration(configurationFile);
	}

	public boolean getDebuggingState() {
		return this.configuration.getBoolean("debug");
	}
}
