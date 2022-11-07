package com.cyprias.chunkspawnerlimiter.listeners;

import com.cyprias.chunkspawnerlimiter.utils.ChatUtil;
import com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter;
import com.cyprias.chunkspawnerlimiter.configs.CslConfig;
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
        if (event.isCancelled() || !CslConfig.Properties.WATCH_BLOCK_PLACE)
            return;

        if (CslConfig.EXCLUDED_WORLDS.contains(event.getBlock().getChunk().getWorld().getName()))
            return;

        final Material placedType = event.getBlock().getType();
        if (plugin.getBlocksConfig().hasLimit(placedType)) {
            final Integer limit = plugin.getBlocksConfig().getLimit(placedType);
            int amountInChunk = countBlocksInChunk(event.getBlock().getChunk().getChunkSnapshot(), placedType);
            if (amountInChunk >= limit) {
                ChatUtil.message(event.getPlayer(), CslConfig.Messages.MAX_AMOUNT_BLOCKS
                        .replace("{material}", placedType.name())
                        .replace("{amount}", String.valueOf(limit)));
                event.setCancelled(true);
            }
            ChatUtil.debug("Material=%s, Count=%d, Limit=%d", placedType, amountInChunk, limit);
        }
    }

    private int countBlocksInChunk(final ChunkSnapshot chunkSnapshot, final Material material) {
        int count = 0;
        for (int x = 0; x < 16; x++) {
            for (int y = -64; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    if (chunkSnapshot.getBlockType(x, y, z) == material)
                        count++;
                }
            }
        }
        return count;
    }

}
