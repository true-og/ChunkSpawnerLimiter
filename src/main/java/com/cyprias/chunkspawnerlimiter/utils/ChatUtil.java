package com.cyprias.chunkspawnerlimiter.utils;

import com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter;
import com.cyprias.chunkspawnerlimiter.configs.CslConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * ChatUtil
 */
public class ChatUtil {
    private ChatUtil() {
        throw new UnsupportedOperationException();
    }

    public static void message(@NotNull CommandSender target, String message) {
        target.sendMessage(colorize(message));
    }

    public static void message(@NotNull CommandSender target, String message, Object... args) {
        target.sendMessage(String.format(colorize(message), args));
    }

    public static void title(@NotNull Player player, final String message) {
        player.sendTitle(colorize(message),"",10,70,20);
    }

    @Contract("_ -> new")
    public static @NotNull String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void debug(String message) {
        if (CslConfig.Properties.DEBUG_MESSAGES) {
            ChunkSpawnerLimiter.getInstance().getLogger().info(() -> "DEBUG " + message);
        }
    }

    public static void debug(String message, Object... args) {
        ChatUtil.debug(String.format(message,args));
    }


}
