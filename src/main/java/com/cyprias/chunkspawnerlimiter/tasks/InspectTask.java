package com.cyprias.chunkspawnerlimiter.tasks;

import com.cyprias.chunkspawnerlimiter.Plugin;
import org.bukkit.Chunk;
import org.bukkit.scheduler.BukkitRunnable;

import static com.cyprias.chunkspawnerlimiter.listeners.WorldListener.checkChunk;

/**
 * @author sarhatabaot
 */
public class InspectTask extends BukkitRunnable {
    private Chunk c;
    private int taskID;

    public InspectTask(Chunk c) {
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

    public void setId(int taskID) {
        this.taskID = taskID;
    }
}
