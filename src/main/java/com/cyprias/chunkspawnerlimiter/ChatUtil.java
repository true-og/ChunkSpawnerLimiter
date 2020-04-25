package com.cyprias.chunkspawnerlimiter;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * ChatUtil
 */
public class ChatUtil {
	public static void tell(CommandSender toWhom, String message) {
		toWhom.sendMessage(colorize(message));
	}

	public static String colorize(String message){
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static void debug(String message) {
		if(Config.Properties.DEBUG_MESSAGES){
			ChunkSpawnerLimiter.getInstance().getLogger().info("DEBUG " + message);
		}
	}


}
