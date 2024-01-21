package com.flintstqne.cropxp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CropXP extends JavaPlugin {
    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        // Set default values for xpCrops and xpAmounts
        Map<String, Integer> defaultXpValues = new HashMap<>();
        defaultXpValues.put("PUMPKIN", 20);
        defaultXpValues.put("MELON", 20);
        defaultXpValues.put("POTATOES", 10);
        defaultXpValues.put("CARROTS", 10);
        defaultXpValues.put("COCOA", 3);
        defaultXpValues.put("BEETROOTS", 10);
        defaultXpValues.put("WHEAT", 10);
        defaultXpValues.put("NETHER_WART", 10);
        // Add other crops and their default XP values here

        // Set default values in the configuration
        config.addDefault("xpCrops", new ArrayList<>(Arrays.asList("PUMPKIN", "MELON", "POTATOES", "CARROTS", "COCOA", "BEETROOTS", "WHEAT", "NETHER_WART")));
        config.addDefault("xpAmounts", defaultXpValues);

        // Copy default values to the config if they don't exist
        config.options().copyDefaults(true);

        // Save the configuration
        saveConfig();

        // Register the BlockChangeListener with the configuration and plugin instance
        this.getServer().getPluginManager().registerEvents(new BlockChangeListener(config, this), this);
        // Register the ReloadCommand
        getCommand("cropxp").setExecutor(new ReloadCommand(this));
    }
}
