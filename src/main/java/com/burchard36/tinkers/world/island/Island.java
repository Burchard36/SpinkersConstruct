package com.burchard36.tinkers.world.island;

import com.burchard36.tinkers.Tinkers;

import java.io.File;

public interface Island {

    void registerPluginInstance(final Tinkers tinkers);
    File getSchematicFile();
}
