package com.cyprias.chunkspawnerlimiter;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bstats.bukkit.Metrics;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.cyprias.chunkspawnerlimiter.listeners.EntityListener;
import com.cyprias.chunkspawnerlimiter.listeners.WorldListener;

public class Plugin extends JavaPlugin {
    private static Plugin instance = null;
    public static Logger logger;

    public void onEnable() {
        instance = this;
        logger = getLogger();

        saveDefaultConfig();
        this.getConfig().options().header(getName()+" v"+getDescription().getVersion()+" config.yml");

        getServer().getPluginManager().registerEvents(new EntityListener(), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);

        checkForMissingProperties();

        Metrics metrics = new Metrics(this);
    }

    private File getConfigFromJar(){
        return new File(getClass().getResource("config.yml").getFile());
    }

    public void checkForMissingProperties(){
        FileConfiguration currentConfig = Plugin.getInstance().getConfig();
        FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(getConfigFromJar());

        for (String property : defaultConfig.getKeys(true)) {
            if (!currentConfig.contains(property))
                Plugin.getInstance().getLogger().warning(property + " is missing from your config.yml, using default.");
        }
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
