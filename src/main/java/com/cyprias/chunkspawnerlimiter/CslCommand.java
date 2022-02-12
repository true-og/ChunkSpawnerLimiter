package com.cyprias.chunkspawnerlimiter;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

@CommandAlias("csl")
public class CslCommand extends BaseCommand {

    @Subcommand("reload")
    @CommandAlias("cslreload")
    @CommandPermission("csl.reload")
    @Description("Reloads the config file.")
    public void onReload(final CommandSender sender){
        ChunkSpawnerLimiter.getInstance().reloadConfig();
        Config.reload();
        ChatUtil.tell(sender,Config.Messages.RELOADED_CONFIG);
    }


    @Subcommand("settings")
    @CommandAlias("cslsettings")
    @CommandPermission("csl.settings")
    @Description("Shows config settings.")
    public void onSettings(final CommandSender sender) {
        ChatUtil.tell(sender, "&2&l-- ChunkSpawnerLimiter v%s --",ChunkSpawnerLimiter.getInstance().getDescription().getVersion());
        ChatUtil.tell(sender,"&2&l-- Properties --");
        ChatUtil.tell(sender,"Debug Message: %s", Config.Properties.DEBUG_MESSAGES);
        ChatUtil.tell(sender,"Check Chunk Load: %s", Config.Properties.CHECK_CHUNK_LOAD);
        ChatUtil.tell(sender,"Check Chunk Unload: %s", Config.Properties.CHECK_CHUNK_UNLOAD);
        ChatUtil.tell(sender,"Active Inspection: %s",Config.Properties.ACTIVE_INSPECTIONS);
        ChatUtil.tell(sender,"Watch Creature Spawns: %s",Config.Properties.WATCH_CREATURE_SPAWNS);
        ChatUtil.tell(sender,"Check Surrounding Chunks: %s", Config.Properties.CHECK_SURROUNDING_CHUNKS);
        ChatUtil.tell(sender,"Inspection Frequency: %d",Config.Properties.INSPECTION_FREQUENCY);
        ChatUtil.tell(sender,"Notify Players: %s",Config.Properties.NOTIFY_PLAYERS);
        ChatUtil.tell(sender,"Preserve Named Entities: %s",Config.Properties.PRESERVE_NAMED_ENTITIES);
        ChatUtil.tell(sender,"Ignore Metadata: %s",Config.Properties.IGNORE_METADATA.toString());
        ChatUtil.tell(sender,"Excluded Worlds: %s",Config.EXCLUDED_WORLDS);
        ChatUtil.tell(sender,"&2&l-- Messages --");
        ChatUtil.tell(sender,"Reloaded Config: %s",Config.Messages.RELOADED_CONFIG);
        ChatUtil.tell(sender,"Removed Entities: %s",Config.Messages.REMOVED_ENTITIES);
    }

    @Subcommand("info")
    @CommandAlias("cslinfo")
    @CommandPermission("csl.info")
    @Description("Shows config info.")
    public void onInfo(final CommandSender sender) {
        ChatUtil.tell(sender, "&2&l-- ChunkSpawnerLimiter v%s --",ChunkSpawnerLimiter.getInstance().getDescription().getVersion());
        ChatUtil.tell(sender,"&2&l-- Reasons to cull on: --");
        sendConfigurationSection(sender,Config.getSpawnReasons());
        ChatUtil.tell(sender,"&2&l-- Entity Limits: --");
        sendConfigurationSection(sender,Config.getEntityLimits());
    }

    private void sendConfigurationSection(final CommandSender sender,final ConfigurationSection section) {
        for(Map.Entry<String,Object> entry: section.getValues(false).entrySet()) {
            ChatUtil.tell(sender,"%s: %s",entry.getKey(),entry.getValue().toString());
        }
    }
}
