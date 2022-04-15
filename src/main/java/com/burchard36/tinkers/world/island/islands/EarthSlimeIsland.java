package com.burchard36.tinkers.world.island.islands;

import com.burchard36.tinkers.Tinkers;
import com.burchard36.tinkers.world.island.Island;

import java.io.File;

public class EarthSlimeIsland implements Island {

    private Tinkers instance;

    @Override
    public void registerPluginInstance(Tinkers tinkers) {
        this.instance = tinkers;
    }

    @Override
    public File getSchematicFile() {
        return new File(this.instance.getDataFolder(), "");
    }
}
