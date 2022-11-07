package com.cyprias.chunkspawnerlimiter.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter;
import com.cyprias.chunkspawnerlimiter.configs.CslConfig;
import com.cyprias.chunkspawnerlimiter.messages.Command;
import com.cyprias.chunkspawnerlimiter.utils.ChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@CommandAlias("csl")
public class CslCommand extends BaseCommand {

    @Subcommand(Command.Reload.COMMAND)
    @CommandAlias(Command.Reload.ALIAS)
    @CommandPermission(Command.Reload.PERMISSION)
    @Description(Command.Reload.DESCRIPTION)
    public void onReload(final CommandSender sender){
        ChunkSpawnerLimiter.getInstance().reloadConfigs();
        CslConfig.reload();
        ChatUtil.message(sender, CslConfig.Messages.RELOADED_CONFIG);
    }


    @Subcommand(Command.Settings.COMMAND)
    @CommandAlias(Command.Settings.ALIAS)
    @CommandPermission(Command.Settings.PERMISSION)
    @Description(Command.Settings.DESCRIPTION)
    public void onSettings(final CommandSender sender) {
        ChatUtil.message(sender, "&2&l-- ChunkSpawnerLimiter v%s --",ChunkSpawnerLimiter.getInstance().getDescription().getVersion());
        ChatUtil.message(sender,"&2&l-- Properties --");
        ChatUtil.message(sender,"Debug Message: %s", CslConfig.Properties.DEBUG_MESSAGES);
        ChatUtil.message(sender,"Check Chunk Load: %s", CslConfig.Properties.CHECK_CHUNK_LOAD);
        ChatUtil.message(sender,"Check Chunk Unload: %s", CslConfig.Properties.CHECK_CHUNK_UNLOAD);
        ChatUtil.message(sender,"Active Inspection: %s", CslConfig.Properties.ACTIVE_INSPECTIONS);
        ChatUtil.message(sender,"Watch Creature Spawns: %s", CslConfig.Properties.WATCH_CREATURE_SPAWNS);
        ChatUtil.message(sender,"Check Surrounding Chunks: %s", CslConfig.Properties.CHECK_SURROUNDING_CHUNKS);
        ChatUtil.message(sender,"Inspection Frequency: %d", CslConfig.Properties.INSPECTION_FREQUENCY);
        ChatUtil.message(sender,"Notify Players: %s", CslConfig.Properties.NOTIFY_PLAYERS);
        ChatUtil.message(sender,"Preserve Named Entities: %s", CslConfig.Properties.PRESERVE_NAMED_ENTITIES);
        ChatUtil.message(sender,"Ignore Metadata: %s", CslConfig.Properties.IGNORE_METADATA.toString());
        ChatUtil.message(sender,"Excluded Worlds: %s", CslConfig.EXCLUDED_WORLDS);
        ChatUtil.message(sender,"&2&l-- Messages --");
        ChatUtil.message(sender,"Reloaded Config: %s", CslConfig.Messages.RELOADED_CONFIG);
        ChatUtil.message(sender,"Removed Entities: %s", CslConfig.Messages.REMOVED_ENTITIES);
    }

    @Subcommand(Command.Info.COMMAND)
    @CommandAlias(Command.Info.ALIAS)
    @CommandPermission(Command.Info.PERMISSION)
    @Description(Command.Info.DESCRIPTION)
    public void onInfo(final CommandSender sender) {
        ChatUtil.message(sender, "&2&l-- ChunkSpawnerLimiter v%s --",ChunkSpawnerLimiter.getInstance().getDescription().getVersion());
        ChatUtil.message(sender,"&2&l-- Reasons to cull on: --");
        sendConfigurationSection(sender, CslConfig.getSpawnReasons());
        ChatUtil.message(sender,"&2&l-- Entity Limits: --");
        sendConfigurationSection(sender, CslConfig.getEntityLimits());
    }

    private void sendConfigurationSection(final CommandSender sender,final @NotNull ConfigurationSection section) {
        for(Map.Entry<String,Object> entry: section.getValues(false).entrySet()) {
            ChatUtil.message(sender,"%s: %s",entry.getKey(),entry.getValue().toString());
        }
    }
}
