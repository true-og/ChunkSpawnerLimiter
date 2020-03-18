package com.cyprias.chunkspawnerlimiter.tasks;

import com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter;
import com.cyprias.chunkspawnerlimiter.Common;
import lombok.Setter;
import org.bukkit.Chunk;
import org.bukkit.scheduler.BukkitRunnable;

import static com.cyprias.chunkspawnerlimiter.listeners.WorldListener.checkChunk;

public class InspectTask extends BukkitRunnable {
    private Chunk chunk;
    @Setter
    private int id;

    public InspectTask(Chunk chunk) {
        this.chunk = chunk;
    }

    @Override
    public void run() {
        Common.debug("Active check " + chunk.getX() + " " + chunk.getZ());
        if (!chunk.isLoaded()) {
            ChunkSpawnerLimiter.cancelTask(id);
            return;
        }
        checkChunk(chunk);
    }
}
