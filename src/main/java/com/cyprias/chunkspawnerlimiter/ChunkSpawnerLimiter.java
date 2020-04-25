package com.cyprias.chunkspawnerlimiter;

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
	@Getter
	@Setter
	private static ChunkSpawnerLimiter instance;

	@Override
	public void onEnable() {
		setInstance(this);
		saveDefaultConfig();
		registerListeners();
		getCommand("cslreload").setExecutor(this);

		new Metrics(this, 4195);
	}


	@Override
	public void onDisable() {
		setInstance(null);
		getServer().getScheduler().cancelTasks(this);
	}


	private void registerListeners() {
		PluginManager manager = getServer().getPluginManager();
		manager.registerEvents(new EntityListener(), this);
		manager.registerEvents(new WorldListener(this), this);
		ChatUtil.debug("Registered listeners.");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		reloadConfig();
		Config.reload();
		sender.sendMessage(ChatUtil.colorize(Config.Messages.RELOADED_CONFIG));
		return true;
	}

	public static void cancelTask(int taskID) {
		Bukkit.getServer().getScheduler().cancelTask(taskID);
	}


}
