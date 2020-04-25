package com.cyprias.chunkspawnerlimiter.tasks;

import com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter;
import com.cyprias.chunkspawnerlimiter.ChatUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Chunk;
import org.bukkit.scheduler.BukkitRunnable;

import static com.cyprias.chunkspawnerlimiter.listeners.WorldListener.checkChunk;

@RequiredArgsConstructor
public class InspectTask extends BukkitRunnable {
    private final Chunk chunk;
    @Setter
    private int id;

    @Override
    public void run() {
        ChatUtil.debug("Active check " + chunk.getX() + " " + chunk.getZ());
        if (!chunk.isLoaded()) {
            ChunkSpawnerLimiter.cancelTask(id);
            return;
        }
        checkChunk(chunk);
    }
}
