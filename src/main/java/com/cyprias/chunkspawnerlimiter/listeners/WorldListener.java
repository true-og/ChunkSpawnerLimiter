package com.cyprias.chunkspawnerlimiter.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.cyprias.chunkspawnerlimiter.utils.ChatUtil;
import com.cyprias.chunkspawnerlimiter.messages.Debug;
import com.cyprias.chunkspawnerlimiter.tasks.InspectTask;
import lombok.RequiredArgsConstructor;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.cyprias.chunkspawnerlimiter.configs.CslConfig;
import com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter;
import com.cyprias.chunkspawnerlimiter.compare.MobGroupCompare;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class WorldListener implements Listener {
    private final ChunkSpawnerLimiter plugin;
    private final Map<Chunk, Integer> chunkTasks = new HashMap<>();

    @EventHandler
    public void onChunkLoadEvent(@NotNull ChunkLoadEvent event) {
        ChatUtil.debug(Debug.CHUNK_LOAD_EVENT,event.getChunk().getX(),event.getChunk().getZ());
        if (CslConfig.Properties.ACTIVE_INSPECTIONS) {
            InspectTask inspectTask = new InspectTask(event.getChunk());
            long delay = CslConfig.Properties.INSPECTION_FREQUENCY * 20L;
            BukkitTask task = inspectTask.runTaskTimer(plugin, delay, delay);
            inspectTask.setId(task.getTaskId());
            chunkTasks.put(event.getChunk(), task.getTaskId());
        }

        if (CslConfig.Properties.CHECK_CHUNK_LOAD)
            checkChunk(event.getChunk());
    }

    @EventHandler
    public void onChunkUnloadEvent(@NotNull ChunkUnloadEvent event) {
        ChatUtil.debug(Debug.CHUNK_UNLOAD_EVENT,event.getChunk().getX(),event.getChunk().getZ());

        if (chunkTasks.containsKey(event.getChunk())) {
            plugin.getServer().getScheduler().cancelTask(chunkTasks.get(event.getChunk()));
            chunkTasks.remove(event.getChunk());
        }

        if (CslConfig.Properties.CHECK_CHUNK_UNLOAD)
            checkChunk(event.getChunk());
    }

    /**
     * Checks the chunk for entities, removes entities if over the limit.
     *
     * @param chunk Chunk
     */
    public static void checkChunk(@NotNull Chunk chunk) {
        if (CslConfig.EXCLUDED_WORLDS.contains(chunk.getWorld().getName())) {
            return;
        }

        Entity[] entities = chunk.getEntities();
        HashMap<String, ArrayList<Entity>> types = addEntitiesByConfig(entities);

        for (Entry<String, ArrayList<Entity>> entry : types.entrySet()) {
            String entityType = entry.getKey();
            int limit = CslConfig.getEntityLimit(entityType);

            if (entry.getValue().size() > limit) {
                ChatUtil.debug(Debug.REMOVING_ENTITY_AT,entry.getValue().size() - limit,entityType,chunk.getX(),chunk.getZ());
                if (CslConfig.Properties.NOTIFY_PLAYERS) {
                    notifyPlayers(entry, entities, limit, entityType);
                }
                removeEntities(entry, limit);
            }
        }
    }

    private static boolean hasCustomName(Entity entity) {
        if (CslConfig.Properties.PRESERVE_NAMED_ENTITIES)
            return entity.getCustomName()!=null;
        return false;
    }

    private static boolean hasMetaData(Entity entity) {
        for (String metadata : CslConfig.Properties.IGNORE_METADATA) {
            if (entity.hasMetadata(metadata)) {
                return true;
            }
        }
        return false;
    }

    private static void removeEntities(@NotNull Entry<String, ArrayList<Entity>> entry, int limit) {
        for (int i = entry.getValue().size() - 1; i >= limit; i--) {
            final Entity entity = entry.getValue().get(i);
            if (hasMetaData(entity) || hasCustomName(entity) || (entity instanceof Player))
                continue;
            entity.remove();
        }
    }

    private static @NotNull HashMap<String, ArrayList<Entity>> addEntitiesByConfig(Entity @NotNull [] entities) {
        HashMap<String, ArrayList<Entity>> modifiedTypes = new HashMap<>();
        for (int i = entities.length - 1; i >= 0; i--) {
            EntityType type = entities[i].getType();

            String entityType = type.name();
            String entityMobGroup = MobGroupCompare.getMobGroup(entities[i]);

            if (CslConfig.contains("entities." + entityType)) {
                modifiedTypes.putIfAbsent(entityType,new ArrayList<>());
                modifiedTypes.get(entityType).add(entities[i]);
            }

            if (CslConfig.contains("entities." + entityMobGroup)) {
                modifiedTypes.putIfAbsent(entityMobGroup,new ArrayList<>());
                modifiedTypes.get(entityMobGroup).add(entities[i]);
            }
        }
        return modifiedTypes;
    }


    private static void notifyPlayers(Entry<String, ArrayList<Entity>> entry, Entity @NotNull [] entities, int limit, String entityType) {
        for (int i = entities.length - 1; i >= 0; i--) {
            if (entities[i] instanceof Player) {
                final Player p = (Player) entities[i];

                ChatUtil.tell(p, CslConfig.Messages.REMOVED_ENTITIES, entry.getValue().size() - limit, entityType);
            }
        }
    }
}
