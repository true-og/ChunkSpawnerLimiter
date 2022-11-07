package com.cyprias.chunkspawnerlimiter.config;

import com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author sarhatabaot
 */
public class BlocksConfig extends ConfigFile<ChunkSpawnerLimiter> {
    private final Map<Material, Integer> materialLimits;

    public BlocksConfig(final @NotNull ChunkSpawnerLimiter plugin) {
        super(plugin, "", "blocks.yml", "");

        this.materialLimits = convertToMaterialLimits(config.getConfigurationSection("blocks").getValues(false));
    }

    public Map<Material, Integer> getMaterialLimits() {
        return materialLimits;
    }

    public Integer getLimit(final Material material) {
        return materialLimits.get(material);
    }

    public boolean hasLimit(final Material material) {
        return materialLimits.containsKey(material);
    }

    private @NotNull Map<Material, Integer> convertToMaterialLimits(@NotNull Map<String, Object> map) {
        final Map<Material, Integer> materialIntegerEnumMap = new EnumMap<>(Material.class);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            final Material material = Material.getMaterial(entry.getKey());

            if (material == null) {
                plugin.getLogger().warning("Incorrect material name, check your blocks.yml and make sure it's set exactly.");
                plugin.getLogger().warning("Skipping entry (material=" + entry.getKey() + ")");
                continue;
            }

            final Integer limit = (Integer) entry.getValue();
            if(limit == null) {
                plugin.getLogger().warning("Missing limit value for material = "+material.name()+", skipping entry.");
                continue;
            }
            materialIntegerEnumMap.put(material,limit);
        }

        return materialIntegerEnumMap;
    }
}
