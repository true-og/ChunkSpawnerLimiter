package com.cyprias.chunkspawnerlimiter.listeners;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.cyprias.chunkspawnerlimiter.Config;
import com.cyprias.chunkspawnerlimiter.Plugin;

public class EntityListener implements Listener {

	@EventHandler
	public void onCreatureSpawnEvent(CreatureSpawnEvent e) {
		if (e.isCancelled())
			return;

		if (!Config.getBoolean("properties.watch-creature-spawns") )
			return;

		String reason = e.getSpawnReason().toString();
		
		if (!Config.getBoolean("spawn-reasons."+reason) || !Config.getBoolean("spawn-reasons."+reason)){
			Plugin.debug("Igonring " + e.getEntity().getType().toString() + " due to spawnreason " + reason);
			return;
		}
		
		//CreatureSpawnEvent.SpawnReason.
		
		// LivingEntity ent = e.getEntity();

		// EntityType t = ent.getType();
		// String eType = t.toString();
		// String eGroup = MobGroupCompare.getMobGroup(ent);

		Chunk c = e.getLocation().getChunk();

		WorldListener.checkChunk(c);

		int surrounding = Config.getInt("properties.check-surrounding-chunks");

		if (surrounding > 0) {
			World w = e.getLocation().getWorld();
			for (int x = c.getX() + surrounding; x >= (c.getX() - surrounding); x--) {
				for (int z = c.getZ() + surrounding; z >= (c.getZ() - surrounding); z--) {
					// Logger.debug("Checking chunk " + x + " " +z);
					WorldListener.checkChunk(w.getChunkAt(x, z));
				}
			}

		}
	}
}
