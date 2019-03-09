package com.cyprias.chunkspawnerlimiter.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.cyprias.chunkspawnerlimiter.tasks.InspectTask;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.cyprias.chunkspawnerlimiter.Config;
import com.cyprias.chunkspawnerlimiter.Plugin;
import com.cyprias.chunkspawnerlimiter.compare.MobGroupCompare;
import org.bukkit.scheduler.BukkitTask;

public class WorldListener implements Listener {
    private HashMap<Chunk, Integer> chunkTasks = new HashMap<>();

    @EventHandler
    public void onChunkLoadEvent(ChunkLoadEvent e) {
        Plugin.debug("ChunkLoadEvent " + e.getChunk().getX() + " " + e.getChunk().getZ());
        if (Config.getBoolean("properties.active-inspections")) {
            InspectTask inspectTask = new InspectTask(e.getChunk());
            long delay = Config.getInt("properties.inspection-frequency") * 20L;
            BukkitTask task = inspectTask.runTaskTimer(Plugin.getInstance(),delay,delay);
            inspectTask.setId(task.getTaskId());
            chunkTasks.put(e.getChunk(), task.getTaskId());
        }

        if (Config.getBoolean("properties.check-chunk-load"))
            checkChunk(e.getChunk());
    }

    @EventHandler
    public void onChunkUnloadEvent(ChunkUnloadEvent e) {
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

        HashMap<String, ArrayList<Entity>> types = addEntitiesByConfig(entities);

        for (Entry<String, ArrayList<Entity>> entry : types.entrySet()) {
            String entityType = entry.getKey();
            int limit = Config.getInt("entities." + entityType);
            if (entry.getValue().size() > limit) {
                Plugin.debug("Removing " + (entry.getValue().size() - limit) + " " + entityType + " @ " + c.getX() + " " + c.getZ());
                if (Config.getBoolean("properties.notify-players")) {
                    notifyPlayers(entry, entities, limit, entityType);
                }
                removeEntities(entry,limit);
            }
        }
    }
    // return a new entry and set the old one
    // entry = removeEntities(); TODO:
    private static void removeEntities(Entry<String, ArrayList<Entity>> entry,int limit){
        for (int i = entry.getValue().size() - 1; i >= limit; i--) {
            entry.getValue().get(i).remove();
        }
    }

    private static HashMap<String, ArrayList<Entity>> addEntitiesByConfig(Entity[] entities){
        HashMap<String, ArrayList<Entity>> modifiedTypes = new HashMap<>();
        for (int i = entities.length - 1; i >= 0; i--) {
            EntityType t = entities[i].getType();

            String entityType = t.toString();
            String eGroup = MobGroupCompare.getMobGroup(entities[i]);

            if (Config.contains("entities." + entityType)) {
                if (!modifiedTypes.containsKey(entityType))
                    modifiedTypes.put(entityType, new ArrayList<>());
                modifiedTypes.get(entityType).add(entities[i]);
            }

            if (Config.contains("entities." + eGroup)) {
                if (!modifiedTypes.containsKey(eGroup))
                    modifiedTypes.put(eGroup, new ArrayList<>());
                modifiedTypes.get(eGroup).add(entities[i]);
            }
        }
        return modifiedTypes;
    }

    private static void notifyPlayers(Entry<String, ArrayList<Entity>> entry, Entity[] entities, int limit,String entityType) {
            for (int i = entities.length - 1; i >= 0; i--) {
                if (entities[i] instanceof Player) {
                    Player p = (Player) entities[i];
                    send(p, Config.getString("messages.removedEntities", entry.getValue().size() - limit, entityType));
                }
            }
    }

    private static void send(CommandSender sender, String message) {
        String newMessage = ChatColor.translateAlternateColorCodes('&',message);
        String[] messages = newMessage.split("\n");
        sender.sendMessage(messages);
    }


}
