package com.burchard36.tinkers.world.island.generators;

import com.burchard36.Logger;
import com.burchard36.tinkers.world.island.Island;
import com.burchard36.tinkers.world.island.IslandGenerationUtils;
import com.burchard36.tinkers.world.island.IslandRegistry;
import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import java.io.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class GenerateIslandTask implements Listener {

    private final IslandRegistry registry;
    private final ThreadLocalRandom random;

    public GenerateIslandTask(final IslandRegistry islandRegistry) {
        this.registry = islandRegistry;
        this.random = ThreadLocalRandom.current();
        this.registry.getPlugin().getServer().getPluginManager().registerEvents(this, this.registry.getPlugin());
    }

    @EventHandler
    public void onChunkLoad(final ChunkLoadEvent event) {
        final Chunk chunk = event.getChunk();
        if (!event.isNewChunk()) return;
        final World.Environment chunkEnvironment = chunk.getWorld().getEnvironment();
        final List<Island> allowedTypes = this.registry.getAllowedIslandsForEnvironment(chunkEnvironment);
        for (final Island island : allowedTypes) {
            int chanceToGenerate = island.chanceToGenerate();
            if (!(random.nextInt(100) >= chanceToGenerate)) continue;

            switch (chunkEnvironment) {

                case NORMAL -> {
                    final CompletableFuture<Location> loc = IslandGenerationUtils.getRandomOverworldIslandLocation(chunk, island);
                    final Location location = loc.join();
                    if (location == null) continue; // location get failed, don't do anything
                    pasteIsland(location, island);
                }
            }
        }
    }

    private void pasteIsland(final Location loc, final Island island) {
        final File file = island.getSchematicFile();
        final World world = loc.getWorld();
        try {
            final Clipboard clipboard = ClipboardFormats
                    .findByFile(file)
                    .load(file);
            clipboard.paste(FaweAPI.getWorld(world.getName()), BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
