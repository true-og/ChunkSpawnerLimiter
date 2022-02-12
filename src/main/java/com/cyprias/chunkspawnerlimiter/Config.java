package com.cyprias.chunkspawnerlimiter;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Config {
	private static FileConfiguration fileConfiguration = ChunkSpawnerLimiter.getInstance().getConfig();
	public static List<String> EXCLUDED_WORLDS = fileConfiguration.getStringList("excluded-worlds");

	private Config() {
		throw new UnsupportedOperationException();
	}


	public static void reload() {
		fileConfiguration = ChunkSpawnerLimiter.getInstance().getConfig();
	}

	public static int getEntityLimit(String entityType) {
		return fileConfiguration.getInt("entities." + entityType);
	}

	public static boolean isSpawnReason(String reason) {
		return fileConfiguration.getBoolean("spawn-reasons." + reason);
	}

	public static boolean contains(String property) {
		return fileConfiguration.contains(property);
	}

	public static ConfigurationSection getSpawnReasons() {
		return fileConfiguration.getConfigurationSection("spawn-reasons");
	}

	public static ConfigurationSection getEntityLimits() {
		return fileConfiguration.getConfigurationSection("entities");
	}

	public static class Properties {
		private static String path = "properties.";
		public static boolean DEBUG_MESSAGES = fileConfiguration.getBoolean(path + "debug-messages");
		public static boolean CHECK_CHUNK_LOAD = fileConfiguration.getBoolean(path + "check-chunk-load");
		public static boolean CHECK_CHUNK_UNLOAD = fileConfiguration.getBoolean(path + "check-chunk-unload");
		public static boolean ACTIVE_INSPECTIONS = fileConfiguration.getBoolean(path + "active-inspections");
		public static boolean WATCH_CREATURE_SPAWNS = fileConfiguration.getBoolean(path + "watch-creature-spawns");
		public static int CHECK_SURROUNDING_CHUNKS = fileConfiguration.getInt(path + "check-surrounding-chunks");
		public static int INSPECTION_FREQUENCY = fileConfiguration.getInt(path + "inspection-frequency");
		public static boolean NOTIFY_PLAYERS = fileConfiguration.getBoolean(path + "notify-players");
		public static boolean PRESERVE_NAMED_ENTITIES = fileConfiguration.getBoolean(path + "preserve-named-entities");
		public static List<String> IGNORE_METADATA = fileConfiguration.getStringList(path + "ignore-metadata");

		private Properties() {
			throw new UnsupportedOperationException();
		}
	}

	public static class Messages {
		private static String path = "messages.";
		public static String REMOVED_ENTITIES = fileConfiguration.getString(path + "removedEntities");
		public static String RELOADED_CONFIG = fileConfiguration.getString(path + "reloadedConfig", "&cReloaded csl config.");

		private Messages() {
			throw new UnsupportedOperationException();
		}
	}


}
