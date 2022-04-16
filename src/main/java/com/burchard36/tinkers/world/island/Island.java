package com.burchard36.tinkers.world.island;

import com.burchard36.tinkers.Tinkers;
import org.bukkit.World;

import java.io.File;
import java.util.List;

public interface Island {

    void registerPluginInstance(final Tinkers tinkers);
    File getSchematicFile();
    List<World.Environment> allowedGenerationEnvironments();
    IslandType getIslandType();
    int chanceToGenerate();
    int highestYValue();
}
