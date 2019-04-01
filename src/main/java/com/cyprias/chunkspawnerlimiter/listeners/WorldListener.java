package com.cyprias.chunkspawnerlimiter.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.cyprias.chunkspawnerlimiter.debug.Debug;
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
            BukkitTask task = inspectTask.runTaskTimer(Plugin.getInstance(), delay, delay);
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

    // Doesn't quite do what it's supposed to, should check if an entity has a custom name, and if it does, don't remove it.
    private static boolean hasCustomName(Entry<String, ArrayList<Entity>> entry) {
        boolean isCustomName = false;
        for (Entity entity : entry.getValue()) {
            isCustomName = !entity.getCustomName().isEmpty();
        }
        return isCustomName;
    }

    /* Doesn't work, should be under remove entities.*/
    private static boolean hasMetadata(Entry<String, ArrayList<Entity>> entry) {
        for (Entity entity : entry.getValue()) {
            for (String metadata : Config.getStringList("properties.ignore-metadata")) {
                if (entity.hasMetadata(metadata)) {
                    Plugin.debug("HasMetaData-" + metadata + ": " + entity.hasMetadata(metadata));
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean hasMetaData(Entity entity){
        for (String metadata : Config.getStringList("properties.ignore-metadata")) {
            if (entity.hasMetadata(metadata)) {
                return true;
            }
        }
        return false;
    }


    public static void checkChunk(Chunk c) {
        if (Config.getStringList("excluded-worlds").contains(c.getWorld().getName())) {
            return;
        }

        Entity[] entities = c.getEntities();

        Plugin.debug(c.getX() + " " + c.getZ() + " Entities:" + Debug.Entities(entities));

        HashMap<String, ArrayList<Entity>> types = addEntitiesByConfig(entities);

        for (Entry<String, ArrayList<Entity>> entry : types.entrySet()) {
            if (Config.getBoolean("properties.preserve-name-entities") && hasCustomName(entry)) {
                //TODO: Still doesn't work - hasCustomName should just include the Config check
                continue;
            }

            String entityType = entry.getKey();

            int limit = Config.getInt("entities." + entityType);

            //TODO: Seems to not add "Villager/NPC" to the list.
            Plugin.debug(entityType + " :limit=" + limit);

            if (entry.getValue().size() > limit) {
                Plugin.debug("Removing " + (entry.getValue().size() - limit) + " " + entityType + " @ " + c.getX() + " " + c.getZ());
                if (Config.getBoolean("properties.notify-players")) {
                    notifyPlayers(entry, entities, limit, entityType);
                }
                removeEntities(entry, limit);
            }
        }
    }

    // return a new entry and set the old one
    // entry = removeEntities(); TODO:
    private static void removeEntities(Entry<String, ArrayList<Entity>> entry, int limit) {
        for (int i = entry.getValue().size() - 1; i >= limit; i--) {
            if(hasMetaData(entry.getValue().get(i)))
                continue;
            entry.getValue().get(i).remove();
        }
    }

    private static HashMap<String, ArrayList<Entity>> addEntitiesByConfig(Entity[] entities) {
        HashMap<String, ArrayList<Entity>> modifiedTypes = new HashMap<>();
        for (int i = entities.length - 1; i >= 0; i--) {
            EntityType type = entities[i].getType();

            String entityType = type.name();
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

    private static void notifyPlayers(Entry<String, ArrayList<Entity>> entry, Entity[] entities, int limit, String entityType) {
        for (int i = entities.length - 1; i >= 0; i--) {
            if (entities[i] instanceof Player) {
                Player p = (Player) entities[i];
                send(p, Config.getString("messages.removedEntities", entry.getValue().size() - limit, entityType));
            }
        }
    }

    private static void send(CommandSender sender, String message) {
        String newMessage = ChatColor.translateAlternateColorCodes('&', message);
        String[] messages = newMessage.split("\n");
        sender.sendMessage(messages);
    }


}
