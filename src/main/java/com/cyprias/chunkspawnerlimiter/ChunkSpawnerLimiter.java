package com.cyprias.chunkspawnerlimiter;

import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.cyprias.chunkspawnerlimiter.listeners.EntityListener;
import com.cyprias.chunkspawnerlimiter.listeners.WorldListener;

public class ChunkSpawnerLimiter extends JavaPlugin {
    @Getter @Setter
    private static ChunkSpawnerLimiter instance;

    @Override
    public void onEnable() {
        setInstance(this);
        saveDefaultConfig();
        registerListeners();
        getCommand("cslreload").setExecutor(this);

        new Metrics(this,4195);
    }



    @Override
    public void onDisable() {
        setInstance(null);
        getServer().getScheduler().cancelTasks(this);
    }


    private void registerListeners(){
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new EntityListener(),this);
        manager.registerEvents(new WorldListener(this),this);
        Common.debug("Registered listeners.");
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if(sender.hasPermission("csl.reload")) {
            reloadConfig();
            sender.sendMessage(Common.colorize("[CSL] Reloaded config."));
            return true;
        }
        sender.sendMessage(Common.colorize(String.format("[CSL] You do not have the &6%s permission.", command.getPermission())));
        return false;
    }

    public static void cancelTask(int taskID) {
        instance.getServer().getScheduler().cancelTask(taskID);
    }


}
