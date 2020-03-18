package com.cyprias.chunkspawnerlimiter.listeners;

import com.cyprias.chunkspawnerlimiter.Common;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.cyprias.chunkspawnerlimiter.Config;

/**
 *
 * Spawn Reasons at https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/event/entity/CreatureSpawnEvent.SpawnReason.html
 */
public class EntityListener implements Listener {
    @EventHandler
    public void onCreatureSpawnEvent(CreatureSpawnEvent e) {
        if (e.isCancelled() || !Config.getBoolean("properties.watch-creature-spawns") )
            return;

        String reason = e.getSpawnReason().toString();

        if (!Config.isSpawnReason(reason)){
            Common.debug("Ignoring " + e.getEntity().getType().toString() + " due to spawn-reason: " + reason);
            return;
        }

        Chunk chunk = e.getLocation().getChunk();
        WorldListener.checkChunk(chunk);
        checkSurroundings(chunk,e.getLocation().getWorld());
    }

    private void checkSurroundings(Chunk chunk,World world){
        int surrounding = Config.Properties.CHECK_SURROUNDING_CHUNKS;
        if (surrounding > 0) {
            for (int x = chunk.getX() + surrounding; x >= (chunk.getX() - surrounding); x--) {
                for (int z = chunk.getZ() + surrounding; z >= (chunk.getZ() - surrounding); z--) {
                    WorldListener.checkChunk(world.getChunkAt(x, z));
                }
            }
        }
    }
}
