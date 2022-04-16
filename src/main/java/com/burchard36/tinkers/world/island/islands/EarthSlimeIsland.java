package com.burchard36.tinkers.world.island.islands;

import com.burchard36.tinkers.Tinkers;
import com.burchard36.tinkers.world.island.Island;
import com.burchard36.tinkers.world.island.IslandType;
import com.comphenix.protocol.utility.MinecraftVersion;
import org.bukkit.World;

import java.io.File;
import java.util.List;

public class EarthSlimeIsland implements Island {

    private Tinkers instance;

    @Override
    public void registerPluginInstance(Tinkers tinkers) {
        this.instance = tinkers;
    }

    @Override
    public File getSchematicFile() {
        return new File(this.instance.getDataFolder(), "schems/slime_earth_1.schem");
    }

    @Override
    public List<World.Environment> allowedGenerationEnvironments() {
        return List.of(World.Environment.NORMAL);
    }

    @Override
    public IslandType getIslandType() {
        return IslandType.SLIME_EARTH;
    }

    @Override
    public int chanceToGenerate() {
        return 5;
    }

    @Override
    public int highestYValue() {
        if (MinecraftVersion.getCurrentVersion().isAtLeast(MinecraftVersion.CAVES_CLIFFS_1)) {
            return 230;
        } else return 300;
    }
}
