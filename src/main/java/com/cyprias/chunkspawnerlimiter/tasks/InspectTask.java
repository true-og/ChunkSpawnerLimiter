package com.cyprias.chunkspawnerlimiter.tasks;

import com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter;
import com.cyprias.chunkspawnerlimiter.utils.ChatUtil;
import com.cyprias.chunkspawnerlimiter.messages.Debug;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.ref.WeakReference;

import static com.cyprias.chunkspawnerlimiter.listeners.WorldListener.checkChunk;

public class InspectTask extends BukkitRunnable {
    private final WeakReference<Chunk> refChunk; //todo suspect memory leak
    private int id;

    @Override
    public void run() {
        final Chunk chunk = this.refChunk.get();
        if (chunk == null) {
            Bukkit.getLogger().fine("Chunk is null! Ignoring");
            return;
        }

        ChatUtil.debug(Debug.ACTIVE_CHECK, chunk.getX(), chunk.getZ());
        if (!chunk.isLoaded()) {
            ChunkSpawnerLimiter.cancelTask(id);
            return;
        }
        checkChunk(chunk);
    }

    public InspectTask(final Chunk chunk) {
        this.refChunk = new WeakReference<>(chunk);
    }

    public void setId(final int id) {
        this.id = id;
    }
}
