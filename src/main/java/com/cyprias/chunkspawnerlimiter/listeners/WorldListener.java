package com.cyprias.chunkspawnerlimiter.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.cyprias.chunkspawnerlimiter.ChatUtils;
import com.cyprias.chunkspawnerlimiter.Config;
import com.cyprias.chunkspawnerlimiter.Plugin;
import com.cyprias.chunkspawnerlimiter.compare.MobGroupCompare;

public class WorldListener implements Listener {
    private HashMap<Chunk, Integer> chunkTasks = new HashMap<>();

    class inspectTask extends BukkitRunnable {
        Chunk c;

        public inspectTask(Chunk c) {
            this.c = c;
        }

        @Override
        public void run() {
            Plugin.debug("Active check " + c.getX() + " " + c.getZ());
            if (!c.isLoaded()) {
                Plugin.cancelTask(taskID);
                return;
            }

            checkChunk(c);
        }

        int taskID;

        public void setId(int taskID) {
            this.taskID = taskID;

        }

    }

    @EventHandler
    public void onChunkLoadEvent(final ChunkLoadEvent e) {
        Plugin.debug("ChunkLoadEvent " + e.getChunk().getX() + " " + e.getChunk().getZ());
        if (Config.getBoolean("properties.active-inspections")) {
            inspectTask task = new inspectTask(e.getChunk());
            int taskID = Plugin.scheduleSyncRepeatingTask(task, Config.getInt("properties.inspection-frequency") * 20L);
            task.setId(taskID);

            chunkTasks.put(e.getChunk(), taskID);
        }

        if (Config.getBoolean("properties.check-chunk-load"))
            checkChunk(e.getChunk());
    }

    @EventHandler
    public void onChunkUnloadEvent(final ChunkUnloadEvent e) {
        Plugin.debug("ChunkUnloadEvent " + e.getChunk().getX() + " " + e.getChunk().getZ());

        if (chunkTasks.containsKey(e.getChunk())) {
            Plugin.getInstance().getServer().getScheduler().cancelTask(chunkTasks.get(e.getChunk()));
            chunkTasks.remove(e.getChunk());
        }

        if (Config.getBoolean("properties.check-chunk-unload"))
            checkChunk(e.getChunk());
    }


    public static void checkChunk(Chunk c) {
        if (Config.getStringList("excluded-worlds").contains(c.getWorld().getName())) {
            return;
        }

        Entity[] entities = c.getEntities();

        HashMap<String, ArrayList<Entity>> types = new HashMap<>();

        for (int i = entities.length - 1; i >= 0; i--) {
            EntityType t = entities[i].getType();

            String entityType = t.toString();
            String eGroup = MobGroupCompare.getMobGroup(entities[i]);

            if (Config.contains("entities." + entityType)) {
                if (!types.containsKey(entityType))
                    types.put(entityType, new ArrayList<>());

                types.get(entityType).add(entities[i]);
            }

            if (Config.contains("entities." + eGroup)) {
                if (!types.containsKey(eGroup))
                    types.put(eGroup, new ArrayList<>());
                types.get(eGroup).add(entities[i]);
            }
        }

        for (Entry<String, ArrayList<Entity>> entry : types.entrySet()) {
            String eType = entry.getKey();
            int limit = Config.getInt("entities." + eType);

            // Logger.debug(c.getX() + " " + c.getZ() + ": " + eType + " = " +
            // entry.getValue().size());

            if (entry.getValue().size() > limit) {
                Plugin.debug("Removing " + (entry.getValue().size() - limit) + " " + eType + " @ " + c.getX() + " " + c.getZ());

                notifyPlayers(entry, entities);

                for (int i = entry.getValue().size() - 1; i >= limit; i--) {
                    entry.getValue().get(i).remove();
                }

            }

        }


    }

    private static void notifyPlayers(Entry<String, ArrayList<Entity>> entry, Entity[] entities) {
        String eType = entry.getKey();
        int limit = Config.getInt("entities." + eType);
        if (Config.getBoolean("properties.notify-players")) {
            for (int i = entities.length - 1; i >= 0; i--) {
                if (entities[i] instanceof Player) {
                    Player p = (Player) entities[i];
                    ChatUtils.send(p, Config.getString("messages.removedEntites", entry.getValue().size() - limit, eType));
                }
            }
        }
    }


}
