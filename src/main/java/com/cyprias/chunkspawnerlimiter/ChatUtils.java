package com.cyprias.chunkspawnerlimiter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Deprecated
public class ChatUtils {
	public static void broadcast(String format, Object... args) {
		broadcast(String.format(format, args));
	}

	public static void broadcast(String message) {
		message = ChatColor.translateAlternateColorCodes('&',message);
		String[] messages = message.split("\n");
		System.arraycopy(messages, 0, messages, 0, messages.length);
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(messages);
		}
		Bukkit.getConsoleSender().sendMessage(messages);
	}

	public static void broadcastRaw(String message) {
		//ChatColor.translateAlternateColorCodes()
		message = ChatColor.translateAlternateColorCodes('&',message);
		String[] messages = message.split("\n");
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(messages);
		}
		Bukkit.getConsoleSender().sendMessage(messages);
	}



}