package com.cyprias.chunkspawnerlimiter;

import java.util.logging.Logger;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import com.cyprias.chunkspawnerlimiter.listeners.EntityListener;
import com.cyprias.chunkspawnerlimiter.listeners.WorldListener;

public class Plugin extends JavaPlugin {
    private static Plugin instance = null;
    private static Logger logger;

    public void onEnable() {
        instance = this;
        logger = getLogger();

        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new EntityListener(), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);

        Metrics metrics = new Metrics(this);
    }


    public static void debug(String msg){
        if(Config.getBoolean("properties.debug-messages")){
            Plugin.logger.info("[debug] " + msg);
        }
    }

    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }

    public static Plugin getInstance() {
        return instance;
    }


    @Deprecated
    public static int scheduleSyncRepeatingTask(Runnable run, long delay) {
        return scheduleSyncRepeatingTask(run, delay, delay);
    }

    @Deprecated
    public static int scheduleSyncRepeatingTask(Runnable run, long start, long delay) {
        return instance.getServer().getScheduler().scheduleSyncRepeatingTask(instance, run, start, delay);
    }

    public static void cancelTask(int taskID) {
        instance.getServer().getScheduler().cancelTask(taskID);
    }


}
