package com.cyprias.chunkspawnerlimiter;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Config {


	public static void reload(){
		config = ChunkSpawnerLimiter.getInstance().getConfig();
	}

	private static FileConfiguration config = ChunkSpawnerLimiter.getInstance().getConfig();
	public static List<String> EXCLUDED_WORLDS = config.getStringList("excluded-worlds");

	public static class Properties {
		private static String path = "properties.";
		public static boolean DEBUG_MESSAGES = config.getBoolean(path+"debug-messages");
		public static boolean CHECK_CHUNK_LOAD = config.getBoolean(path+"check-chunk-load");
		public static boolean CHECK_CHUNK_UNLOAD = config.getBoolean(path+"check-chunk-unload");
		public static boolean ACTIVE_INSPECTIONS = config.getBoolean(path+"active-inspections");
		public static boolean WATCH_CREATURE_SPAWNS = config.getBoolean(path+"watch-creature-spawns");
		public static int CHECK_SURROUNDING_CHUNKS = config.getInt(path+"check-surrounding-chunks");
		public static int INSPECTION_FREQUENCY = config.getInt(path+"inspection-frequency");
		public static boolean NOTIFY_PLAYERS = config.getBoolean(path+"notify-players");
		public static boolean PRESERVE_NAMED_ENTITIES = config.getBoolean(path+"preserve-named-entities");
		public static List<String> IGNORE_METADATA = config.getStringList(path+"ignore-metadata");
	}

	public static class Messages {
		private static String path = "messages.";
		public static String REMOVED_ENTITIES = config.getString(path+"removedEntities");
	}
	public static boolean getBoolean(String property) {
		return ChunkSpawnerLimiter.getInstance().getConfig().getBoolean(property);
	}

	public static int getEntityLimit(String entityType) {
		return config.getInt("entities."+entityType);
	}


	public static boolean isSpawnReason(String reason) {
		return config.getBoolean("spawn-reasons."+reason);
	}

	public static String getString(String property, Object... args) {
		return String.format(config.getString(property), args);
	}

	
	public static boolean contains(String property) {
		return config.contains(property);
	}
	
	public static List<String> getStringList(String property) {
		return config.getStringList(property);
	}

}
