package com.cyprias.chunkspawnerlimiter.configs;

import com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class CslConfig {
	private static FileConfiguration fileConfiguration = ChunkSpawnerLimiter.getInstance().getConfig();
	public static List<String> EXCLUDED_WORLDS = fileConfiguration.getStringList("excluded-worlds");

	private CslConfig() {
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
		public static boolean WATCH_VEHICLE_CREATE = fileConfiguration.getBoolean(path + "watch-vehicle-spawns");
		public static int CHECK_SURROUNDING_CHUNKS = fileConfiguration.getInt(path + "check-surrounding-chunks");
		public static int INSPECTION_FREQUENCY = fileConfiguration.getInt(path + "inspection-frequency", 300);
		public static boolean NOTIFY_PLAYERS = fileConfiguration.getBoolean(path + "notify-players", false);
		public static boolean PRESERVE_NAMED_ENTITIES = fileConfiguration.getBoolean(path + "preserve-named-entities", true);
		public static List<String> IGNORE_METADATA = fileConfiguration.getStringList(path + "ignore-metadata");

		public static boolean WATCH_BLOCK_PLACE = fileConfiguration.getBoolean(path + "watch-block-place", true);

		private Properties() {
			throw new UnsupportedOperationException();
		}
	}

	public static class Messages {
		private static String path = "messages.";
		public static String REMOVED_ENTITIES = fileConfiguration.getString(path + "removedEntities");
		public static String RELOADED_CONFIG = fileConfiguration.getString(path + "reloadedConfig", "&cReloaded csl config.");

		public static String MAX_AMOUNT_BLOCKS = fileConfiguration.getString(path + "maxAmountBlocks", "&6Cannot place more &4{material}&6. Max amount per chunk &2{amount}.");
		public static String MAX_AMOUNT_BLOCKS_TITLE = fileConfiguration.getString(path + "maxAmountBlocksTitle","&6Cannot place more &4{material}&6.");
		public static String MAX_AMOUNT_BLOCKS_SUBTITLE= fileConfiguration.getString(path + "maxAmountBlocksSubtitle","&6Max amount per chunk &2{amount}.");

		private Messages() {
			throw new UnsupportedOperationException();
		}
	}


}
