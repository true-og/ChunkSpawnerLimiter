package com.cyprias.chunkspawnerlimiter;

import co.aikar.commands.PaperCommandManager;
import com.cyprias.chunkspawnerlimiter.listeners.EntityListener;
import com.cyprias.chunkspawnerlimiter.listeners.WorldListener;
import com.cyprias.chunkspawnerlimiter.messages.Debug;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ChunkSpawnerLimiter extends JavaPlugin {
	private static ChunkSpawnerLimiter instance;

	public static void setInstance(final ChunkSpawnerLimiter plugin) {
		ChunkSpawnerLimiter.instance = plugin;
	}

	@Override
	public void onEnable() {
		setInstance(this);
		saveDefaultConfig();
		registerListeners();
		PaperCommandManager paperCommandManager = new PaperCommandManager(this);
		paperCommandManager.registerCommand(new CslCommand());
		new Metrics(this, 4195);
	}

	@Override
	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
		setInstance(null);
	}

	public static ChunkSpawnerLimiter getInstance() {
		return instance;
	}

	private void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new EntityListener(), this);
		pm.registerEvents(new WorldListener(this), this);
		ChatUtil.debug(Debug.REGISTER_LISTENERS);
	}

	public static void cancelTask(int taskID) {
		Bukkit.getServer().getScheduler().cancelTask(taskID);
	}

}
