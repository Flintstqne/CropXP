package com.flintstqne.cropxp;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BlockChangeListener implements Listener {
    private static final ArrayList<Material> materials = new ArrayList<>();
    private static final List<BlockFace> blockFaces = List.of(BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH);


    List<Material> cropOptions = List.of(Material.POTATOES, Material.CARROTS, Material.COCOA, Material.BEETROOTS, Material.WHEAT, Material.MELON, Material.PUMPKIN, Material.NETHER_WART);
    int xpAmount;

    public BlockChangeListener(FileConfiguration config, Plugin plugin) {
        // Retrieve default XP values for crops from the configuration
        Map<String, Integer> defaultXpValues = (Map<String, Integer>) config.get("xpAmounts");

        // Set default XP amount for crops
        for (String material : config.getStringList("xpCrops")) {
            if (Material.getMaterial(material) == null) {
                plugin.getLogger().severe("Invalid material type, skipping!");
                continue;
            }

            if (cropOptions.contains(Material.getMaterial(material))) {
                materials.add(Material.getMaterial(material));
                xpAmount = defaultXpValues.getOrDefault(material, config.getInt("xpAmount"));
            } else {
                plugin.getLogger().warning("Unsupported crop type, skipping!");
            }
        }

        // If xpAmount is still 0, log a warning
        if (xpAmount < 1) {
            xpAmount = 0;
            plugin.getLogger().severe("Invalid XP amount, default set to 0");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void blockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Material material = block.getType();
        if (materials.contains(material)) {
            if (material == Material.MELON || material == Material.PUMPKIN) {
                for (BlockFace direction : blockFaces) {
                    Block adjacent = block.getRelative(direction);
                    if (adjacent.getType() == Material.ATTACHED_MELON_STEM || adjacent.getType() == Material.ATTACHED_PUMPKIN_STEM) {
                        Directional stemDirection = (Directional) adjacent.getBlockData();
                        if (stemDirection.getFacing() == direction.getOppositeFace()) {
                            event.setExpToDrop(xpAmount);
                        }
                        return;
                    }
                }
            } else {
                final Ageable ageable = (Ageable) block.getBlockData();
                if (ageable.getAge() == ageable.getMaximumAge()) {
                    event.setExpToDrop(xpAmount);
                }
            }
        } else if ((material == Material.ATTACHED_MELON_STEM && materials.contains(Material.MELON)) || (material == Material.ATTACHED_PUMPKIN_STEM && materials.contains(Material.PUMPKIN))) {
            event.setExpToDrop(xpAmount);
        }
    }
}
