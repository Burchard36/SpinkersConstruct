package com.burchard36.tinkers.world.island;

import com.comphenix.protocol.utility.MinecraftVersion;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.concurrent.CompletableFuture;

public class IslandGenerationUtils {

    public static CompletableFuture<Location> getRandomOverworldIslandLocation(final Chunk chunk,
                                                                                final Island island) {
        final ChunkSnapshot snap = chunk.getChunkSnapshot();
        for (int x = 0; x <= 15; x++) {
            for (int z = 0; z <= 15; z++) {
                for (int y = island.highestYValue(); y >= 0; y--) {

                    if (snap.getBlockType(x, y, z) != Material.AIR) continue;
                    final int locX = x;
                    final int locY = y;
                    final int locZ = z;
                    return CompletableFuture.supplyAsync(() -> chunk.getBlock(locX, locY, locZ).getLocation());
                }
            }
        }

        return CompletableFuture.supplyAsync(() -> null);
    }

}
