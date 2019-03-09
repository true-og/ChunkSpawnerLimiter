package com.cyprias.chunkspawnerlimiter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatUtils {
	private static final String colorCodes;

	public static void broadcast(String format, Object... args) {
		broadcast(String.format(format, args));
	}

	public static void broadcast(String message) {
		message = replaceColorCodes(message);
		String[] messages = message.split("\n");
		System.arraycopy(messages, 0, messages, 0, messages.length);
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(messages);
		}
		Bukkit.getConsoleSender().sendMessage(messages);
	}

	public static void broadcastRaw(String format, Object... args) {
		broadcastRaw(String.format(format, args));
	}

	public static void broadcastRaw(String message) {
		//ChatColor.translateAlternateColorCodes()
		message = replaceColorCodes(message);
		String[] messages = message.split("\n");
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(messages);
		}
		Bukkit.getConsoleSender().sendMessage(messages);
	}

	public static void send(CommandSender sender, String message) {
		message = replaceColorCodes(message);
		String[] messages = message.split("\n");
		sender.sendMessage(messages);
	}


	public static void sendRaw(CommandSender sender, ChatColor color, String format, Object... args) {
		sender.sendMessage(color + String.format(format, args));
	}

	public static void sendCommandHelp(CommandSender sender, String line, org.bukkit.command.Command cmd) {
		sendCommandHelp(sender, "- ", line, cmd);
	}

	public static void sendCommandHelp(CommandSender sender, String prefix, String line, org.bukkit.command.Command cmd) {
		sendRaw(sender, ChatColor.GRAY, prefix + line, cmd.getLabel());
	}



	public static void errorRaw(CommandSender sender, String format, Object... args) {
		sender.sendMessage(ChatColor.RED + String.format(format, args));
	}


	// replace color codes with the colors
	// use ChatColor.translateAlternateColorCodes() instead
	@Deprecated
	public static String replaceColorCodes(String mess) {
		return mess.replaceAll("(&([" + colorCodes + "]))", "\u00A7$2");
	}

	// get rid of color codes
	// use ChatColor.stripColor() instead
	@Deprecated
	public static String cleanColorCodes(String mess) {
		return mess.replaceAll("(&([" + colorCodes + "]))", "");
	}

	// TODO: Gets the ChatColor values, just use ChatColor instead
	static {
		String string = "";
		for (ChatColor color : ChatColor.values()) {
			char c = color.getChar();
			if (!Character.isLetter(c)) {
				string += c;
			} else {
				string += Character.toUpperCase(c);
				string += Character.toLowerCase(c);
			}
		}
		colorCodes = string;
	}
}