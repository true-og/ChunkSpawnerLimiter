package com.cyprias.chunkspawnerlimiter.configs;

import com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CslConfig extends ConfigFile<ChunkSpawnerLimiter>{
	private Messages messages;
	private Properties properties;
	private List<String> excludedWorlds;


	public CslConfig(final @NotNull ChunkSpawnerLimiter plugin) {
		super(plugin, "", "config.yml", "");
	}

	@Override
	public void initValues() {
		this.excludedWorlds = config.getStringList("excluded-worlds");
		this.messages = new Messages();
		this.properties = new Properties();
	}

	public int getEntityLimit(String entityType) {
		return config.getInt("entities." + entityType);
	}

	public boolean isSpawnReason(String reason) {
		return config.getBoolean("spawn-reasons." + reason);
	}

	public boolean contains(String property) {
		return config.contains(property);
	}

	public ConfigurationSection getSpawnReasons() {
		return config.getConfigurationSection("spawn-reasons");
	}

	public ConfigurationSection getEntityLimits() {
		return config.getConfigurationSection("entities");
	}

	public class Properties {
		private final String path = "properties.";
		private boolean debugMessages = config.getBoolean(path + "debug-messages");
		private boolean checkChunkLoad = config.getBoolean(path + "check-chunk-load");
		private boolean checkChunkUnload = config.getBoolean(path + "check-chunk-unload");
		private boolean activeInspections = config.getBoolean(path + "active-inspections");
		private boolean watchCreatureSpawns = config.getBoolean(path + "watch-creature-spawns");
		private boolean watchVehicleCreate = config.getBoolean(path + "watch-vehicle-spawns");
		private int checkSurroundingChunks = config.getInt(path + "check-surrounding-chunks");
		private int inspectionFrequency = config.getInt(path + "inspection-frequency", 300);
		private boolean notifyPlayers = config.getBoolean(path + "notify-players", false);
		private boolean preserveNamedEntities = config.getBoolean(path + "preserve-named-entities", true);
		private List<String> ignoreMetadata = config.getStringList(path + "ignore-metadata");

		public boolean isDebugMessages() {
			return debugMessages;
		}

		public boolean isCheckChunkLoad() {
			return checkChunkLoad;
		}

		public boolean isCheckChunkUnload() {
			return checkChunkUnload;
		}

		public  boolean isActiveInspections() {
			return activeInspections;
		}

		public  boolean isWatchCreatureSpawns() {
			return watchCreatureSpawns;
		}

		public  boolean isWatchVehicleCreate() {
			return watchVehicleCreate;
		}

		public  int getCheckSurroundingChunks() {
			return checkSurroundingChunks;
		}

		public  int getInspectionFrequency() {
			return inspectionFrequency;
		}

		public  boolean isNotifyPlayers() {
			return notifyPlayers;
		}

		public  boolean isPreserveNamedEntities() {
			return preserveNamedEntities;
		}

		public  List<String> getIgnoreMetadata() {
			return ignoreMetadata;
		}
	}

	public class Messages {
		private final String path = "messages.";
		private String removedEntities = config.getString(path + "removedEntities");
		private String reloadedConfig = config.getString(path + "reloadedConfig", "&cReloaded csl config.");

		private String maxAmountBlocks = config.getString(path + "maxAmountBlocks", "&6Cannot place more &4{material}&6. Max amount per chunk &2{amount}.");
		private String maxAmountBlocksTitle = config.getString(path + "maxAmountBlocksTitle","&6Cannot place more &4{material}&6.");
		private String maxAmountBlocksSubtitle = config.getString(path + "maxAmountBlocksSubtitle","&6Max amount per chunk &2{amount}.");
		public String getRemovedEntities() {
			return removedEntities;
		}

		public  String getReloadedConfig() {
			return reloadedConfig;
		}

		public  String getMaxAmountBlocks() {
			return maxAmountBlocks;
		}

		public  String getMaxAmountBlocksTitle() {
			return maxAmountBlocksTitle;
		}

		public  String getMaxAmountBlocksSubtitle() {
			return maxAmountBlocksSubtitle;
		}
	}

	public List<String> getExcludedWorlds() {
		return excludedWorlds;
	}

	public Messages getMessages() {
		return messages;
	}

	public Properties getProperties() {
		return properties;
	}
}
