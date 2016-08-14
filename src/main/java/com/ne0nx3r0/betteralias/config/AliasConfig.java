package com.ne0nx3r0.betteralias.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.ne0nx3r0.betteralias.BetterAlias;
import com.ne0nx3r0.betteralias.alias.Alias;
import com.ne0nx3r0.betteralias.alias.AliasCommand;
import com.ne0nx3r0.betteralias.alias.AliasCommandTypes;

public class AliasConfig {

	private final Plugin plugin;

	private final File configurationFile;

	private static FileConfiguration configuration;

	public AliasConfig(final BetterAlias plugin) {

		this.plugin = plugin;
		
		this.configurationFile = new File(plugin.getDataFolder(), "aliases.yml");
		
        if (!configurationFile.exists()) {
        	configurationFile.getParentFile().mkdirs();
            copy(plugin.getResource("aliases.yml"), configurationFile);
        }
		
        AliasConfig.configuration = YamlConfiguration.loadConfiguration(configurationFile);
	}

	public void reload() {
		AliasConfig.configuration = YamlConfiguration.loadConfiguration(configurationFile);
	}

	public FileConfiguration getAliasConfiguration() {
		return AliasConfig.configuration;
	}

    public final HashMap<String, Alias> loadAliases() {
    	HashMap<String, Alias> aliases = new HashMap<String, Alias>();

        Set<String> aliasList = AliasConfig.configuration.getKeys(false);

        if (aliasList.isEmpty()) {
            plugin.getLogger().log(Level.WARNING, "No aliases found in aliases.yml");

            return aliases;
        }

        for (String sAlias : aliasList) {
            Alias alias = new Alias(
                    sAlias,
                    AliasConfig.configuration.getBoolean(sAlias + ".caseSensitive", false),
                    AliasConfig.configuration.getString(sAlias + ".permission", null),
                    AliasConfig.configuration.getString(sAlias + ".priority", null));

            for (String sArg : AliasConfig.configuration.getConfigurationSection(sAlias).getKeys(false)) {
                List<AliasCommand> commandsList = new ArrayList<AliasCommand>();

                if (!sArg.equalsIgnoreCase("permission")
                		&& !sArg.equalsIgnoreCase("caseSensitive")
                		&& !sArg.equalsIgnoreCase("priority")) {
                    int iArg;

                    if (sArg.equals("*")) {
                        iArg = -1;
                    } else {
                    	// TODO This raise error sometime on unknown configuration parameter
                        iArg = Integer.parseInt(sArg);
                    }

                    List<String> sArgLines = new ArrayList<String>();

                    if (AliasConfig.configuration.isList(sAlias + "." + sArg)) {
                        sArgLines = AliasConfig.configuration.getStringList(sAlias + "." + sArg);
                    } else {
                        sArgLines.add(AliasConfig.configuration.getString(sAlias + "." + sArg));
                    }

                    for (String sArgLine : sArgLines) {
                        AliasCommandTypes type = AliasCommandTypes.PLAYER;

                        int waitTime = 0;

                        if (sArgLine.contains(" ")) {
                            String sType = sArgLine.substring(0, sArgLine.indexOf(" "));

                            if (sType.equalsIgnoreCase("console")) {
                                type = AliasCommandTypes.CONSOLE;

                                sArgLine = sArgLine.substring(sArgLine.indexOf(" ") + 1);
                            } else if (sType.equalsIgnoreCase("player_as_op")) {
                                type = AliasCommandTypes.PLAYER_AS_OP;

                                sArgLine = sArgLine.substring(sArgLine.indexOf(" ") + 1);
                            } else if (sType.equalsIgnoreCase("reply")) {
                                type = AliasCommandTypes.REPLY_MESSAGE;

                                sArgLine = sArgLine.substring(sArgLine.indexOf(" ") + 1);
                            } else if (sType.equalsIgnoreCase("wait")) {
                                String[] sArgLineParams = sArgLine.split(" ");

                                try {
                                    waitTime = Integer.parseInt(sArgLineParams[1]);
                                } catch (Exception e) {
                                    plugin.getLogger().log(Level.WARNING, "Invalid wait time for command {0} in alias {1}, skipping line",
                                            new Object[]{sArgLine, sAlias});

                                    continue;
                                }

                                if (sArgLineParams[2].equalsIgnoreCase("reply")) {
                                    type = AliasCommandTypes.WAIT_THEN_REPLY;

                                    sArgLine = sArgLine.replace(sArgLineParams[0] + " " + sArgLineParams[1] + " " + sArgLineParams[2] + " ", "");
                                } else if (sArgLineParams[2].equalsIgnoreCase("console")) {
                                    type = AliasCommandTypes.WAIT_THEN_CONSOLE;

                                    sArgLine = sArgLine.replace(sArgLineParams[0] + " " + sArgLineParams[1] + " " + sArgLineParams[2] + " ", "");
                                } else {
                                    type = AliasCommandTypes.WAIT;

                                    sArgLine = sArgLine.replace(sArgLineParams[0] + " " + sArgLineParams[1] + " ", "");
                                }
                            }
                        }

                        sArgLine = this.replaceColorCodes(sArgLine);

                        commandsList.add(new AliasCommand(sArgLine, type, waitTime));
                    }

                    alias.setCommandsFor(iArg, commandsList);
                }
            }

            aliases.put(sAlias, alias);
        }
        
        return aliases;
    }
    
    private String replaceColorCodes(String str) {
        for (ChatColor cc : ChatColor.values()) {
            str = str.replace("&" + cc.name(), cc.toString());
        }

        return str;
    }

    public void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
