package com.cyprias.ChunkSpawnerLimiter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bstats.bukkit.Metrics;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import com.cyprias.ChunkSpawnerLimiter.listeners.EntityListener;
import com.cyprias.ChunkSpawnerLimiter.listeners.WorldListener;

public class Plugin extends JavaPlugin {
    private static Plugin instance = null;
    public static ChunkLogger logger;
    public static String chatPrefix = "&4[&bCSL&4]&r ";

    public static HashMap<String, Location> deaths = new HashMap<>();

    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        this.getConfig().options().copyDefaults(true); //Copies keys if they aren't in the file
        // Check if the config on disk is missing any settings
        try {
            Config.checkForMissingProperties();
        } catch (IOException e4) {
            e4.printStackTrace();
        } catch (InvalidConfigurationException e4) {
            e4.printStackTrace();
        }

        // Register our event listener.
        getServer().getPluginManager().registerEvents(new EntityListener(), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);

        // Start the Metrics.
        if (Config.getBoolean("properties.use-metrics")) {
            Metrics metrics = new Metrics(this);
        }


    }
    public class ChunkLogger {
        Logger logger = Plugin.getInstance().getLogger();

        public void debug(String msg){
            logger.info("[debug] "+msg);
        }

    }

    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }

    public static final Plugin getInstance() {
        return instance;
    }


    public static int scheduleSyncRepeatingTask(Runnable run, long delay) {
        return scheduleSyncRepeatingTask(run, delay, delay);
    }

    public static int scheduleSyncRepeatingTask(Runnable run, long start, long delay) {
        return instance.getServer().getScheduler().scheduleSyncRepeatingTask(instance, run, start, delay);
    }

    public static void cancelTask(int taskID) {
        instance.getServer().getScheduler().cancelTask(taskID);
    }


}
