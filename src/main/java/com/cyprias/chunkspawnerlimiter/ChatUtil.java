package com.cyprias.chunkspawnerlimiter;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * ChatUtil
 */
public class ChatUtil {
    private ChatUtil() {
        throw new UnsupportedOperationException();
    }

    public static void tell(@NotNull CommandSender toWhom, String message) {
        toWhom.sendMessage(colorize(message));
    }

    public static void tell(@NotNull CommandSender toWhom, String message, Object... args) {
        toWhom.sendMessage(String.format(colorize(message), args));
    }

    @Contract("_ -> new")
    public static @NotNull String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void debug(String message) {
        if (Config.Properties.DEBUG_MESSAGES) {
            ChunkSpawnerLimiter.getInstance().getLogger().info(() -> "DEBUG " + message);
        }
    }

    public static void debug(String message, Object... args) {
        ChatUtil.debug(String.format(message,args));
    }


}
