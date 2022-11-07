package com.cyprias.chunkspawnerlimiter.listeners;

import com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter;
import com.cyprias.chunkspawnerlimiter.config.CslConfig;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * @author sarhatabaot
 */
public class PlaceBlockListener implements Listener {
    private final ChunkSpawnerLimiter plugin;

    public PlaceBlockListener(final ChunkSpawnerLimiter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if(event.isCancelled() || CslConfig.Properties.WATCH_BLOCK_PLACE)
            return;


        final Material placedType = event.getBlock().getType();
        if (plugin.getBlocksConfig().hasLimit(placedType)) {
            final Integer limit = plugin.getBlocksConfig().getLimit(placedType);
            if (limit > countBlocksInChunk(event.getBlock().getChunk().getChunkSnapshot(), placedType)) {

                event.getPlayer().sendMessage(CslConfig.Messages.MAX_AMOUNT_BLOCKS
                        .replace("{material}",placedType.name())
                        .replace("{limit}",String.valueOf(limit)));
                event.setCancelled(true);
            }
        }
    }

    private int countBlocksInChunk(final ChunkSnapshot chunkSnapshot, final Material material) {
        int count = 0;
        for (int x = 0; x < 64; x++) {
            for (int y = 0; y < 64; y++) {
                for (int z = 0; z < 64; z++) {
                    if (chunkSnapshot.getBlockType(x, y, z) == material)
                        count++;
                }
            }
        }
        return count;
    }

}
